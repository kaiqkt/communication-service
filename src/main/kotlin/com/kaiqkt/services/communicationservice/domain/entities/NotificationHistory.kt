package com.kaiqkt.services.communicationservice.domain.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class NotificationHistory(
    @Id
    val id: String,
    val notifications: List<Notification> = listOf(),
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
