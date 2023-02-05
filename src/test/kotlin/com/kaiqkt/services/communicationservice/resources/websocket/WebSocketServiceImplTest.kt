package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class WebSocketServiceImplTest {
    private val publisher: WebSocketPublisher = mockk(relaxed = true)
    private val socket: WebSocketService = WebSocketServiceImpl(publisher)

    @Test
    fun `given an notification to send, should send to queue based on userId`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()

        every { publisher.publish(any(), any()) } just runs

        socket.sendNotification(userId, notification)

        verify { publisher.publish(userId, notification) }
    }
}