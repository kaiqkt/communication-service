package com.kaiqkt.services.communicationservice.resources.springmail

import com.kaiqkt.services.communicationservice.domain.gateways.SpringMailService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.exceptions.UnexpectedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage

@Component
class SpringMailServiceImplementation(
    @Value("\${spring.mail.username}")
    private val senderEmail: String,
    private val emailSender: JavaMailSender
) : SpringMailService {

    override fun send(recipient: String, subject: String, template: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        )

        logger.info("Sending email to $recipient")

        try {
            helper.setTo(recipient)
            helper.setText(template, true)
            helper.setSubject(subject)
            helper.setFrom(senderEmail)

            emailSender.send(message)
            logger.info("Email sent to $recipient successfully")
        } catch (ex: MailException) {
            throw ResourceException("Error sending email to $recipient")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}