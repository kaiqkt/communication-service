package com.kaiqkt.services.communicationservice.resources.aws.sqs

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.services.sqs.model.SendMessageResult
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MessagingServiceImplementationTest {
    private val amazonSQSAsync: AmazonSQSAsync = mockk(relaxed = true)
    private val queueName = "sqs-queue"
    private val messagingService: MessagingService = MessagingServiceImplementation(amazonSQSAsync)

    @Test
    fun `given email, should send successfully to email queue`() {
        val email = EmailSampler.sample()
        val messageRequest = messageRequest(email)

        every { amazonSQSAsync.sendMessage(any()) } returns SendMessageResult()

        messagingService.send(email, queueName)

        verify { amazonSQSAsync.sendMessage(messageRequest) }
    }

    @Test
    fun `given email, when return error, should throw SendToQueueException`() {
        val email = EmailSampler.sample()

        every { amazonSQSAsync.sendMessage(any()) } throws Exception()

        assertThrows<ResourceException> {
            messagingService.send(email, queueName)
        }
    }

    private fun messageRequest(email: Email): SendMessageRequest {
        return SendMessageRequest().apply {
            withQueueUrl(queueName)
            withMessageBody(jacksonObjectMapper().writeValueAsString(email))
        }
    }
}