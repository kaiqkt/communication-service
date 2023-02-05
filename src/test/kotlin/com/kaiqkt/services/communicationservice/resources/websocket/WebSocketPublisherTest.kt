package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.messaging.simp.SimpMessagingTemplate

class WebSocketPublisherTest{
    private val messagingTemplate: SimpMessagingTemplate = mockk(relaxed = true)
    private val publisher: WebSocketPublisher = WebSocketPublisher(messagingTemplate)

    @Test
    fun `given an userId and a notification, should send a notification to private queue based on userId`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()

        every { messagingTemplate.convertAndSendToUser(any(), any(), any()) } just runs

        publisher.publish(userId, notification)

        verify { messagingTemplate.convertAndSendToUser(userId, "/private", notification) }
    }
}