package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.application.handler.ErrorHandler
import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepository
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val webSocketService: WebSocketService,
    private val notificationRepository: NotificationRepository
) {
    fun sendNotification(userId: String, notification: Notification) {
        val hasSessions = WebsocketSessionHolder.validateAndClose(userId)

        if (hasSessions) {
            webSocketService.sendNotification(userId, notification)
            log.info("Notification sent to user via web socket")
        }

        notificationRepository.insert(userId, notification)
        log.info("Notification for user $userId inserted on history successfully")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }
}