package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationResponseV1
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1

fun NotificationV1.toDomain() = Notification(
    title = this.title,
    body = this.body
)

fun Notification.toV1() = NotificationResponseV1(
    title = this.title,
    body = this.body,
    createdAt = this.createdAt
)