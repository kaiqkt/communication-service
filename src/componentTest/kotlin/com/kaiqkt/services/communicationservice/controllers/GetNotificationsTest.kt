package com.kaiqkt.services.communicationservice.controllers

import com.github.kittinunf.fuel.core.Headers
import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.ROLE_USER
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationHistoryV1
import io.azam.ulidj.ULID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetNotificationsTest : ApplicationIntegrationTest() {

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
            .expectBody(NotificationHistoryV1::class.java)
            .consumeWith { response ->
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

}