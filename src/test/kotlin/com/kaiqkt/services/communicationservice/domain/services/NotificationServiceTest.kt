package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.domain.entities.SessionSampler
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepository
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

class NotificationServiceTest {
    private val webSocketService: WebSocketService = mockk(relaxed = true)
    private val webSocketSession: WebSocketSession = mockk(relaxed = true)
    private val repository: NotificationRepository = mockk(relaxed = true)
    private val service: NotificationService = NotificationService(webSocketService, repository)

    @Test
    fun `given an user and an notification, should send and insert in notification history`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val session = SessionSampler.sample(webSocketSession)

        WebsocketSessionHolder.save(userId, session)

        every { webSocketService.sendNotification(any(), any()) } just runs
        every { repository.insert(any(), any()) } just runs

        service.sendNotification(userId, notification)

        verify { webSocketService.sendNotification(userId, notification) }
        verify { repository.insert(userId, notification) }
    }

    @Test
    fun `given an user and an notification, when user not have session, should send and insert in notification history`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()

        every { webSocketService.sendNotification(any(), any()) } just runs
        every { repository.insert(any(), any()) } just runs

        service.sendNotification(userId, notification)

        verify(exactly = 0) { webSocketService.sendNotification(userId, notification) }
        verify { repository.insert(userId, notification) }
    }
}