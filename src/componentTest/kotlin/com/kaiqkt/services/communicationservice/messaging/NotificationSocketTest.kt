package com.kaiqkt.services.communicationservice.messaging

import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.ROLE_USER
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import io.azam.ulidj.ULID
import org.junit.jupiter.api.Test
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import java.lang.reflect.Type
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit


class NotificationSocketTest : ApplicationIntegrationTest() {

    var blockingQueue: BlockingQueue<String> = LinkedBlockingDeque()

    @Test
    @Throws(Exception::class)
    fun getGreeting() {
        val userId: String = ULID.random()
        val token = JWTUtils.generateToken(userId, customerSecret, listOf(ROLE_USER), ULID.random(), 1)
        val headers = WebSocketHttpHeaders().apply {
            add("Authorization", "Bearer $token")
        }


        val session: StompSession = webSocketStompClient
            .connect("ws://localhost:$port/ws", headers, object : StompSessionHandlerAdapter() {})
            .get(1, TimeUnit.SECONDS)

        session.subscribe("/user/$userId/private", handler)
    }

    val handler = object : StompFrameHandler {
        override fun getPayloadType(headers: StompHeaders): Type {
            return ByteArray::class.java
        }

        override fun handleFrame(headers: StompHeaders, payload: Any?) {
            blockingQueue.offer(String((payload as ByteArray?)!!))
        }
    }
}