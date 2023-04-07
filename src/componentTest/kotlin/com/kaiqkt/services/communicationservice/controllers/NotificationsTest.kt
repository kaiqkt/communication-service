package com.kaiqkt.services.communicationservice.controllers

import com.github.kittinunf.fuel.core.Headers
import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.ROLE_USER
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1
import io.azam.ulidj.ULID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.jvm.optionals.getOrNull

class NotificationsTest : ApplicationIntegrationTest() {

    @Test
    fun `given a request to get the notification history, when exist, should return it`(){
        val userId = ULID.random()
        val history = NotificationHistorySampler.sample().copy(id = userId)

        notificationHistoryRepository.save(history)

        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 2)

        webTestClient
            .get()
            .uri("/push")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(NotificationV1::class.java)
            .consumeWith<WebTestClient.ListBodySpec<NotificationV1>> { response ->
                val body = response.responseBody
                Assertions.assertNotNull(body)
            }
    }

    @Test
    fun `given a request to get the notification history, when not exist, should return http status 404`(){
        val userId = ULID.random()

        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 2)

        webTestClient
            .get()
            .uri("/push")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `given a request to update the notification, should return http status 204`(){
        val userId = ULID.random()
        val history = NotificationHistorySampler.sample().copy(id = userId)
        val notificationId = history.notifications.first().id

        notificationHistoryRepository.save(history)

        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 2)

        webTestClient
            .patch()
            .uri("/push/$notificationId")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isNoContent


        val notification = notificationHistoryRepository.findById(userId).getOrNull()!!.notifications.first()

        Assertions.assertEquals(notification.isVisualized, true)
    }
}