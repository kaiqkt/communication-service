package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.entities.Push
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.OneSignalService
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationHistoryRepository
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import org.apache.commons.text.StringSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PushService(
    private val notificationHistoryRepository: NotificationHistoryRepository,
    private val templateFileRepository: TemplateFileRepository,
    private val oneSignalService: OneSignalService,
    private val messagingService: MessagingService,
    @Value("\${aws.sqs.push-queue-name}")
    private val queueName: String,
) {

    fun sendToQueue(push: Push) = messagingService.send(push, queueName)

    fun send(push: Push) {
        try {
            val templateFile = templateFileRepository.find(push.template.url)
            val renderedTemplate = StringSubstitutor(push.template.data).replace(templateFile.content)
            val additionData = mapOf("deepLink" to push.deepLink)

            val notification = Notification(title = push.subject, body = renderedTemplate)

            notificationHistoryRepository.insert(push.recipient, notification).also {
                logger.info("Notification inserted in history of person ${push.recipient}")
            }

            oneSignalService.sendOne(push.recipient, push.subject, renderedTemplate, additionData)

        } catch (ex: Exception) {
            logger.error("Unable to send push, error: {${ex.message}}")
        }
    }

    fun visualizeNotification(userId: String, notificationId: String) {
        notificationHistoryRepository.updateNotification(userId, notificationId)

        logger.info("Notification $notificationId updated in history of person $userId")
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun findNotificationHistory(userId: String): NotificationHistory? {
        val history = notificationHistoryRepository.findById(userId).getOrNull() ?: return null

        history.notifications.ifEmpty { return null }

        return history
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}