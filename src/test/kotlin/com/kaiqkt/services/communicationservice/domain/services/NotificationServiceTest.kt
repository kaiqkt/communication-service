package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.domain.entities.SessionSampler
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationCacheRepository
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepository
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession
import java.util.*
import kotlin.jvm.optionals.getOrNull

class NotificationServiceTest {
    private val webSocketService: WebSocketService = mockk(relaxed = true)
    private val webSocketSession: WebSocketSession = mockk(relaxed = true)
    private val repository: NotificationRepository = mockk(relaxed = true)
    private val cacheRepository: NotificationCacheRepository = mockk(relaxed = true)
    private val service: NotificationService = NotificationService(webSocketService, repository, cacheRepository)

    @Test
    fun `given an user and an notification, should send and insert in notification history and cache`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val history = NotificationHistorySampler.sample()
        val session = SessionSampler.sample(webSocketSession)

        WebsocketSessionHolder.save(userId, session)

        every { webSocketService.sendNotification(any(), any()) } just runs
        every { repository.insert(any(), any()) } returns history
        every { cacheRepository.insert(any(), any()) } just runs

        service.send(userId, notification)

        verify { webSocketService.sendNotification(userId, notification) }
        verify { repository.insert(userId, notification) }
        verify { cacheRepository.insert(userId, history) }
    }

    @Test
    fun `given an user and an notification, when user not have session, should insert in notification history and in the cache`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val history = NotificationHistorySampler.sample()

        every { webSocketService.sendNotification(any(), any()) } just runs
        every { repository.insert(any(), any()) } returns history

        service.send(userId, notification)

        verify(exactly = 0) { webSocketService.sendNotification(userId, notification) }
        verify { repository.insert(userId, notification) }
        verify { cacheRepository.insert(userId, history) }
    }

    @Test
    fun `given an user and an notification, when user not find an history, should not save in cache`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val history = NotificationHistorySampler.sample()

        every { webSocketService.sendNotification(any(), any()) } just runs
        every { repository.insert(any(), any()) } returns null

        service.send(userId, notification)

        verify(exactly = 0) { webSocketService.sendNotification(userId, notification) }
        verify { repository.insert(userId, notification) }
        verify(exactly = 0) { cacheRepository.insert(userId, history) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `given a user id, when exist in cache, should return without call mongo database`() {
        val userId = ULID.random()
        val history = NotificationHistorySampler.sample()

        every { cacheRepository.findByUser(any()) } returns history

        service.find(userId)

        verify { cacheRepository.findByUser(userId) }
        verify(exactly = 0) { repository.findById(userId).getOrNull() }
    }

    @Test
    fun `given a user id, when not exist in cache, should call mongo database and return the history`() {
        val userId = ULID.random()
        val history = NotificationHistorySampler.sample()

        every { cacheRepository.findByUser(any()) } returns null
        every { repository.findById(any()) } returns Optional.of(history)

        service.find(userId)

        verify { cacheRepository.findByUser(userId) }
        verify { repository.findById(userId) }
    }

    @Test
    fun `given a user id, when not exist in cache and in mongo, should return null`() {
        val userId = ULID.random()

        every { cacheRepository.findByUser(any()) } returns null
        every { repository.findById(any()) } returns Optional.ofNullable(null)

        val historyResponse = service.find(userId)

        verify { cacheRepository.findByUser(userId) }
        verify { repository.findById(userId) }

        Assertions.assertNull(historyResponse)
    }
}