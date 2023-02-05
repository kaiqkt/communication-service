package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import org.springframework.stereotype.Component

@Component
class WebSocketServiceImpl(
    private val webSocketPublisher: WebSocketPublisher
) : WebSocketService {
    override fun sendNotification(userId: String, notification: Notification) {
        webSocketPublisher.publish(userId, notification)
    }
}