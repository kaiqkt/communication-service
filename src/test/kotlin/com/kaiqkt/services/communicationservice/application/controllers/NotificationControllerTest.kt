package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.services.communicationservice.application.dto.NotificationV1Sampler
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.application.security.CustomAuthenticationSampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
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
import org.springframework.security.core.context.SecurityContextHolder

class NotificationControllerTest {

    private val service: NotificationService = mockk(relaxed = true)
    private val controller = NotificationController(service)

    @Test
    fun `given an user and a notification, should send and return http 204`() {
        val userId = ULID.random()
        val notification = NotificationV1Sampler.sample()

        every { service.send(any(), any()) } just runs

        val response = controller.sendOne(userId, notification)

        verify { service.send(userId, any()) }
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `given an find notifications based on access token, when exist history of notification, should them with http 200`() {
        val authentication = CustomAuthenticationSampler.sample()
        val history = NotificationHistorySampler.sample()

        SecurityContextHolder.getContext().authentication = authentication

        every { service.find(any()) } returns history

        val response = controller.find()

        verify { service.find(authentication.id!!) }

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `given an find notifications based on access token, when not exist history of notification, should return http 204`() {
        val authentication = CustomAuthenticationSampler.sample()

        SecurityContextHolder.getContext().authentication = authentication

        every { service.find(any()) } returns null

        val response = controller.find()

        verify { service.find(authentication.id!!) }

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}