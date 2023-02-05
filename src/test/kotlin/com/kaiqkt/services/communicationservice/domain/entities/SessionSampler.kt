package com.kaiqkt.services.communicationservice.domain.entities

import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object SessionSampler {

    fun sample(webSocketSession: WebSocketSession) = Session(
        webSocketSession = webSocketSession,
        expireAt = Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant())
    )

    fun expiredSample(webSocketSession: WebSocketSession) = Session(
        webSocketSession = webSocketSession,
        expireAt = Date.from(LocalDateTime.now().minusHours(2).atZone(ZoneId.systemDefault()).toInstant())
    )
}