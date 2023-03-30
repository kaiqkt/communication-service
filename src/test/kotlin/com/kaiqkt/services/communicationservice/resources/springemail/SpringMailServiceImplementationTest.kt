package com.kaiqkt.services.communicationservice.resources.springemail

import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.domain.gateways.SpringMailService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.springmail.SpringMailServiceImplementation
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.internet.MimeMessage

class SpringMailServiceImplementationTest {
    private val senderEmail: String = "shinji@gmail.com"
    private val emailSender: JavaMailSender = mockk(relaxed = true)
    private val springMailService: SpringMailService = SpringMailServiceImplementation(senderEmail, emailSender)

    @Test
    fun `given email, should send successfully`() {
        val email = EmailSampler.sample()
        val message: MimeMessage = emailSender.createMimeMessage()

        every { emailSender.send(message) } just runs

        springMailService.send(email.recipient, email.subject, email.template.toString())

        verify { emailSender.send(message) }
    }

    @Test
    fun `given email, when occur some error, should throw SendEmailException`() {
        val email = EmailSampler.sample()
        val message: MimeMessage = emailSender.createMimeMessage()

        every { emailSender.send(message) } throws MailSendException("error")

        assertThrows<ResourceException> {
            springMailService.send(email.recipient, email.subject, email.template.toString())
        }
    }
}