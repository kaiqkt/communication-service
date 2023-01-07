package com.kaiqkt.services.communicationservice.application.messaging

import com.amazon.sqs.javamessaging.message.SQSTextMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.domain.services.EmailService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.jms.TextMessage

class EmailListenerTest {

    private val emailService: EmailService = mockk(relaxed = true)
    private val emailListener: EmailListener = EmailListener(emailService)

    @Test
    fun `given a message, should convert and send the email successfully`() {
        val request = EmailSampler.sample()
        val message: TextMessage = SQSTextMessage()
        message.text = jacksonObjectMapper().writeValueAsString(request)

        every { emailService.send(any()) } just runs

        emailListener.onMessage(message)

        verify { emailService.send(any()) }
    }
}