package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class WebSocketPublisher(
    private val messagingTemplate: SimpMessagingTemplate
) {
    fun publish(userId: String, notification: Notification) = messagingTemplate.convertAndSendToUser(userId, "/private", notification)
}