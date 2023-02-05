package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.gateways.WebSocketService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationCacheRepository
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepository
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class NotificationService(
    private val webSocketService: WebSocketService,
    private val notificationRepository: NotificationRepository,
    private val cacheRepository: NotificationCacheRepository
) {
    fun send(userId: String, notification: Notification) {
        val hasSessions = WebsocketSessionHolder.validateAndClose(userId)

        if (hasSessions) {
            webSocketService.sendNotification(userId, notification)
            log.info("Notification sent to user via web socket")
        }

        notificationRepository.insert(userId, notification)?.also {
            cacheRepository.insert(userId, it)
            log.info("Notification for user $userId inserted on history successfully")
        }
    }
    @OptIn(ExperimentalStdlibApi::class)
    fun find(userId: String): NotificationHistory? {
        return cacheRepository.findByUser(userId) ?: notificationRepository.findById(userId).getOrNull()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}