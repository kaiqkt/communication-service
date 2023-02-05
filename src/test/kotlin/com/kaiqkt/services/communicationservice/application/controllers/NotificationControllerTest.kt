package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.services.communicationservice.application.dto.NotificationV1Sampler
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.domain.services.NotificationService
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class NotificationControllerTest {

    private val service: NotificationService = mockk(relaxed = true)
    private val controller = NotificationController(service)

    @Test
    fun `given an user and a notification, should send and return http 204`() {
        val userId = ULID.random()
        val notification = NotificationV1Sampler.sample()

        every { service.sendNotification(any(), any()) } just runs

        val response = controller.sendOne(userId, notification)

        verify { service.sendNotification(userId, notification.toDomain()) }
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}