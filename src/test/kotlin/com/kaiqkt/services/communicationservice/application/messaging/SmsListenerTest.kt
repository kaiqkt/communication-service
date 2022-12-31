package com.kaiqkt.services.communicationservice.application.messaging

import com.amazon.sqs.javamessaging.message.SQSTextMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.domain.entities.SmsSampler
import com.kaiqkt.services.communicationservice.domain.services.SmsService
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.jms.TextMessage

class SmsListenerTest {

    private val smsService: SmsService = mockk(relaxed = true)
    private val smsListener: SmsListener = SmsListener(smsService)

    @Test
    fun `given a message, should convert and send the sms successfully`() {
        val request = SmsSampler.sample()
        val message: TextMessage = SQSTextMessage()
        message.text = jacksonObjectMapper().writeValueAsString(request)

        every { smsService.send(any()) } just runs

        smsListener.onMessage(message)

        verify { smsService.send(any()) }
    }
}