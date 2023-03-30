package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.commons.security.auth.getUserId
import com.kaiqkt.services.communicationservice.application.dto.PushV1Sampler
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.application.security.CustomAuthenticationSampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.services.PushService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder

class PushControllerTest {
    private val pushService: PushService = mockk(relaxed = true)
    private val controller = PushController(pushService)

    @Test
    fun `given a push to send, should send to the queue`() {
        val push = PushV1Sampler.sample()

        every { pushService.sendToQueue(any()) } just runs

        controller.sendOne(push)

        verify { pushService.sendToQueue(push.toDomain()) }
    }

    @Test
    fun `given a user id, when exist exist a notification history with notifications, should return the notifications`() {
        val history = NotificationHistorySampler.sample()

        SecurityContextHolder.getContext().authentication = CustomAuthenticationSampler.sample()

        every { pushService.findNotificationHistory(any()) } returns history

        val response = controller.findAll()

        verify { pushService.findNotificationHistory(getUserId()) }

        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(response.statusCode, HttpStatus.OK)
    }

    @Test
    fun `given a user id, when exist not exist notifications in history, should return http status 404`() {
        SecurityContextHolder.getContext().authentication = CustomAuthenticationSampler.sample()

        every { pushService.findNotificationHistory(any()) } returns null

        val response = controller.findAll()

        verify { pushService.findNotificationHistory(getUserId()) }

        Assertions.assertNull(response.body)
        Assertions.assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }
}