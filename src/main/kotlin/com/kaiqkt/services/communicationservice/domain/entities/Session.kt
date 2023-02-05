package com.kaiqkt.services.communicationservice.domain.entities

import org.springframework.web.socket.WebSocketSession
import java.util.*

data class Session(
    val webSocketSession: WebSocketSession,
    val expireAt: Date
)