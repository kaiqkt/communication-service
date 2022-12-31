package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.domain.entities.TemplateFileSampler
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.SpringMailService
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.apache.commons.text.StringSubstitutor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EmailServiceTest {
    private val springMailService: SpringMailService = mockk(relaxed = true)
    private val messagingService: MessagingService = mockk(relaxed = true)
    private val templateFileRepository: TemplateFileRepository = mockk(relaxed = true)
    private val queueName: String = "email-queue"
    private val emailService: EmailService =
        EmailService(springMailService, templateFileRepository, queueName, messagingService)

    @Test
    fun `given a email, should send to email requested successfully`() {
        val email = EmailSampler.sample()
        val templateFile = TemplateFileSampler.sample()
        val renderedTemplateFile = StringSubstitutor(email.template.data).replace(templateFile.content)

        every { templateFileRepository.find(any()) } returns templateFile
        every { springMailService.send(any(), any(), any()) } just runs

        emailService.send(email)

        verify { templateFileRepository.find(email.template.url) }
        verify { springMailService.send(email.recipient, email.subject, renderedTemplateFile) }
    }

    @Test
    fun `given a valid email, when template not found should throw TemplateNotFoundException`() {
        val email = EmailSampler.sample()
        val templateFile = TemplateFileSampler.sample()
        val renderedTemplateFile = StringSubstitutor(email.template.data).replace(templateFile.content)

        every { templateFileRepository.find(any()) } throws ResourceException("Unable to retrieve file")

        assertThrows<ResourceException> {
            emailService.send(email)
        }

        verify { templateFileRepository.find(email.template.url) }
        verify(exactly = 0) { springMailService.send(email.recipient, email.subject, renderedTemplateFile) }
    }

    @Test
    fun `given a valid message, when is converted with success to email, should send to sqs successfully`() {
        val email = EmailSampler.sample()

        every { messagingService.send(any(), any()) } just runs

        emailService.sendToQueue(email)

        verify { messagingService.send(email, queueName) }

    }

    @Test
    fun `given a valid email,when is not possible to send to sqs queue, should throw SendToQueueException`() {
        val email = EmailSampler.sample()

        every { messagingService.send(any(), any()) } throws ResourceException("Error sending message to SQS")

        assertThrows<ResourceException> {
            emailService.sendToQueue(email)
        }

        verify { messagingService.send(email, queueName) }

    }
}