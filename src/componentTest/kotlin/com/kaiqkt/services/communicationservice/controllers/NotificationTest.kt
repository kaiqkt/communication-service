package com.kaiqkt.services.communicationservice.controllers

import com.github.kittinunf.fuel.core.Headers
import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.ROLE_USER
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.application.dto.NotificationV1Sampler
import com.kaiqkt.services.communicationservice.application.ext.mapper
import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationHistoryV1
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import io.azam.ulidj.ULID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import java.lang.reflect.Type
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit
import kotlin.jvm.optionals.getOrNull

class NotificationTest : ApplicationIntegrationTest() {

    val blockingQueue: BlockingQueue<String> = LinkedBlockingDeque()

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `given a user who is connected with websocket, should send an notification and save in mongodb`() {
        val request = NotificationV1Sampler.sample()
        val userId = ULID.random()

        setUpListener(userId)

        webTestClient
            .post()
            .uri("/notification/$userId")
            .contentType(MediaType.parseMediaType("application/vnd.kaiqkt_notification_v1+json"))
            .header(Headers.AUTHORIZATION, serviceSecret)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isNoContent

        val notification = mapper.readValue(blockingQueue.poll(1, TimeUnit.SECONDS), Notification::class.java)

        Assertions.assertEquals(request.title, notification.title)
        Assertions.assertEquals(request.body, notification.body)

        val history = notificationRepository.findById(userId).getOrNull()

        Assertions.assertEquals(history?.notifications?.size, 1)

        val hasSession = WebsocketSessionHolder.validateAndClose(userId)

        Assertions.assertTrue(hasSession)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `given a user who is not connected with websocket, should just save in mongodb`() {
        val request = NotificationV1Sampler.sample()
        val userId = ULID.random()

        webTestClient
            .post()
            .uri("/notification/$userId")
            .contentType(MediaType.parseMediaType("application/vnd.kaiqkt_notification_v1+json"))
            .header(Headers.AUTHORIZATION, serviceSecret)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isNoContent

        Assertions.assertNull(blockingQueue.poll(1, TimeUnit.SECONDS))

        val history = notificationRepository.findById(userId).getOrNull()

        Assertions.assertEquals(history?.notifications?.size, 1)

        val hasSession = WebsocketSessionHolder.validateAndClose(userId)

        Assertions.assertFalse(hasSession)
    }

    @Test
    fun `given a user,when exist a notification history should return them with http status 200`() {
        val notification = NotificationSampler.sample()
        val userId = ULID.random()
        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 1)

        notificationRepository.insert(userId, notification)

        webTestClient
            .get()
            .uri("/notification")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(NotificationHistoryV1::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                Assertions.assertEquals(body?.notifications?.size, 1)
            }
    }

    @Test
    fun `given a user,when exist a notification history in cache, should return them with http status 200`() {
        val history = NotificationHistorySampler.sample()
        val userId = ULID.random()
        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 1)

        cacheNotificationRepository.insert(userId, history)

        webTestClient
            .get()
            .uri("/notification")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(NotificationHistoryV1::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                Assertions.assertEquals(body?.notifications?.size, 1)
            }
    }

    @Test
    fun `given a user,when not exist a notification history, should return http status 204`() {
        val token = JWTUtils.generateToken(ULID.random(), customerSecret, listOf(ROLE_USER), ULID.random(), 1)

        webTestClient
            .get()
            .uri("/notification")
            .header(Headers.AUTHORIZATION, "Bearer $token")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    fun setUpListener(userId: String) {
        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 1)
        val headers = WebSocketHttpHeaders().apply {
            add("Authorization", "Bearer $token")
        }

        val session: StompSession = webSocketStompClient
            .connect("ws://localhost:$port/ws", headers, object : StompSessionHandlerAdapter() {})
            .get(1, TimeUnit.SECONDS)

        session.subscribe("/user/$userId/private", handler())
    }

    fun handler() = object : StompFrameHandler {
        override fun getPayloadType(headers: StompHeaders): Type {
            return ByteArray::class.java
        }

        override fun handleFrame(headers: StompHeaders, payload: Any?) {
            blockingQueue.offer(String((payload as ByteArray?)!!))
        }
    }
}
