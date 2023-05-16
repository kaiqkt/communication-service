package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.entities.PushSampler
import com.kaiqkt.services.communicationservice.domain.entities.TemplateFileSampler
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.OneSignalService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationHistoryRepository
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.azam.ulidj.ULID
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class PushServiceTest {
    private val notificationHistoryRepository: NotificationHistoryRepository = mockk(relaxed = true)
    private val templateFileRepository: TemplateFileRepository = mockk(relaxed = true)
    private val oneSignalService: OneSignalService = mockk(relaxed = true)
    private val messagingService: MessagingService = mockk(relaxed = true)
    private val queueName = "communication-d-sqs-push"
    private val service = PushService(
        notificationHistoryRepository,
        templateFileRepository,
        oneSignalService,
        messagingService,
        queueName
    )

    @Test
    fun `given a push to send, when the queue is up, should send successfully`() {
        val push = PushSampler.sample()

        every { messagingService.send(any(), any()) } just runs

        service.sendToQueue(push)

        verify { messagingService.send(push, queueName) }
    }

    @Test
    fun `given a push to send, when occur a error, should send successfully`() {
        val push = PushSampler.sample()

        every {
            messagingService.send(
                any(),
                any()
            )
        } throws ResourceException("Error sending message to queue $queueName")

        assertThrows<ResourceException> {
            service.sendToQueue(push)
        }

        verify { messagingService.send(push, queueName) }
    }

    @Test
    fun `given a push to send for one signal, when find the template, should send successfully`() {
        val templateFile = TemplateFileSampler.sample()
        val push = PushSampler.sample()

        every { templateFileRepository.find(any()) } returns templateFile
        every { oneSignalService.sendOne(any(), any(), any(), any()) } just runs
        every { notificationHistoryRepository.insert(any(), any()) } just runs

        service.send(push)

        verify { templateFileRepository.find(push.template.url) }
        verify { oneSignalService.sendOne(push.recipient, push.subject, "<html>\${name}</html>", mapOf("deepLink" to push.deepLink!!)) }
        verify { notificationHistoryRepository.insert(push.recipient, any()) }
    }

    @Test
    fun `given a push to send for one signal without deep link, when find the template, should send successfully`() {
        val templateFile = TemplateFileSampler.sample()
        val push = PushSampler.sample().copy(deepLink = null)

        every { templateFileRepository.find(any()) } returns templateFile
        every { oneSignalService.sendOne(any(), any(), any(), any()) } just runs
        every { notificationHistoryRepository.insert(any(), any()) } just runs

        service.send(push)

        verify { templateFileRepository.find(push.template.url) }
        verify { oneSignalService.sendOne(push.recipient, push.subject, "<html>\${name}</html>", mapOf("deepLink" to null)) }
        verify { notificationHistoryRepository.insert(push.recipient, any()) }
    }


    @Test
    fun `given a push to send for one signal, when not find the template, should throw a exception`() {
        val push = PushSampler.sample()

        every { templateFileRepository.find(any()) } throws ResourceException("")

        service.send(push)

        verify { templateFileRepository.find(push.template.url) }
        verify(exactly = 0) { oneSignalService.sendOne(any(), any(), any(), mapOf()) }
        verify(exactly = 0) { notificationHistoryRepository.insert(any(), any()) }
    }

    @Test
    fun `given a push to send for one signal, when fail to send the push to one signal, should throw a exception`() {
        val push = PushSampler.sample()
        val templateFile = TemplateFileSampler.sample()

        every { templateFileRepository.find(any()) } returns templateFile
        every { oneSignalService.sendOne(any(), any(), any(), any()) } throws ResourceException("")

        service.send(push)

        verify { templateFileRepository.find(push.template.url) }
        verify { oneSignalService.sendOne(any(), any(), any(), any()) }
        verify { notificationHistoryRepository.insert(any(), any()) }
    }

    @Test
    fun `given a user id, when exist a notification history with notifications, should return it`() {
        val history = NotificationHistorySampler.sample()
        val userId = ULID.random()

        every { notificationHistoryRepository.findById(any()) } returns Optional.of(history)

        val response = service.findNotificationHistory(userId)

        Assertions.assertNotNull(response)

        verify { notificationHistoryRepository.findById(userId) }
    }

    @Test
    fun `given a user id, when exist not notification history , should return null`() {
        val userId = ULID.random()

        every { notificationHistoryRepository.findById(any()) } returns Optional.empty()

        val response = service.findNotificationHistory(userId)

        Assertions.assertNull(response)

        verify { notificationHistoryRepository.findById(userId) }
    }

    @Test
    fun `given a user id, when exist notification history without notification, should return null`() {
        val userId = ULID.random()
        val history = NotificationHistorySampler.sample().copy(notifications = emptyList())

        every { notificationHistoryRepository.findById(any()) } returns Optional.of(history)

        val response = service.findNotificationHistory(userId)

        Assertions.assertNull(response)

        verify { notificationHistoryRepository.findById(userId) }
    }

    @Test
    fun `given a visualized notification, should update in the notification history`() {
        val userId = ULID.random()
        val notificationId = ULID.random()

        every { notificationHistoryRepository.updateNotification(any(), any()) } just runs

        service.visualizeNotification(userId, notificationId)

        verify { notificationHistoryRepository.updateNotification(userId, notificationId) }
    }
}