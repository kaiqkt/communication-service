package com.kaiqkt.services.communicationservice.application.messaging

import com.amazon.sqs.javamessaging.message.SQSTextMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.domain.entities.PushSampler
import com.kaiqkt.services.communicationservice.domain.services.PushService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class PushListenerTest{
    private val pushService: PushService = mockk(relaxed = true)
    private val listener = PushListener(pushService)

    @Test
    fun `given a message, should convert and send the push successfully`(){
        val request = PushSampler.sample()
        val message = SQSTextMessage()
        message.text = jacksonObjectMapper().writeValueAsString(request)

        every { pushService.send(any()) } just runs

        listener.onMessage(message)

        verify { pushService.send(request) }
    }
}