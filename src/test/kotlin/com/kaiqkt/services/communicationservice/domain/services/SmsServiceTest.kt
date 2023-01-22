package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.SmsSampler
import com.kaiqkt.services.communicationservice.domain.entities.TemplateFileSampler
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.TwilioService
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


class SmsServiceTest {
    private val twilioService: TwilioService = mockk(relaxed = true)
    private val messagingService: MessagingService = mockk(relaxed = true)
    private val templateFileRepository: TemplateFileRepository = mockk(relaxed = true)
    private val queueName = "sms-queueu"
    private val smsService: SmsService = SmsService(twilioService, templateFileRepository, messagingService, queueName)

    @Test
    fun `given a sms, should send to phone number requested successfully`() {
        val sms = SmsSampler.sample()
        val templateFile = TemplateFileSampler.sample()
        val renderedTemplateFile = StringSubstitutor(sms.template.data).replace(templateFile.content)

        every { templateFileRepository.find(any()) } returns templateFile
        every { twilioService.send(any(), any()) } just runs

        smsService.send(sms)

        verify { templateFileRepository.find(sms.template.url) }
        verify { twilioService.send(sms.recipient, renderedTemplateFile) }
    }

    @Test
    fun `given a sms, when template not found should throw TemplateNotFoundException`() {
        val sms = SmsSampler.sample()

        every { templateFileRepository.find(any()) } throws ResourceException("Template ${sms.template.url} not found ")

        smsService.send(sms)

        verify { templateFileRepository.find(sms.template.url) }
        verify(exactly = 0) { twilioService.send(any(), any()) }
    }

    @Test
    fun `given a message, when is converted with success to sms, should send to sqs successfully`() {
        val sms = SmsSampler.sample()

        every { messagingService.send(any(), any()) } just runs

        smsService.sendToQueue(sms)

        verify { messagingService.send(sms, queueName) }

    }

    @Test
    fun `given a valid sms,when is not possible to send to sqs queue, should throw SendToQueueException`() {
        val sms = SmsSampler.sample()

        every { messagingService.send(any(), any()) } throws ResourceException("Error sending message to SQS")

        assertThrows<ResourceException> {
            smsService.sendToQueue(sms)
        }

        verify { messagingService.send(sms, queueName) }
    }
}
