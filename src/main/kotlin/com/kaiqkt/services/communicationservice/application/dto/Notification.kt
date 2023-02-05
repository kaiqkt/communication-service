package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1

fun NotificationV1.toDomain() = Notification(
    title = this.title,
    body = this.body
)