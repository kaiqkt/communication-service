package communicationservice.domain.services

import communicationservice.domain.entities.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.io.IOException
import java.nio.charset.StandardCharsets
import org.thymeleaf.context.Context;
import javax.mail.MessagingException
import javax.mail.internet.MimeMessage


@Service
class EmailService(private val emailSender: JavaMailSender, private val templateEngine: SpringTemplateEngine) {

    @Value("\${spring.mail.username}")
    private lateinit var from: String

    @Throws(MessagingException::class, IOException::class)
    fun sendEmail(email: Email) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())

        val context = Context()
        context.setVariables(email.template.data)

        val html = templateEngine.process(email.template.path, context)

        helper.setTo(email.to)
        helper.setText(html, true)
        helper.setSubject(email.subject)
        helper.setFrom(from)

        emailSender.send(message)
    }
}