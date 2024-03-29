package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.application.ext.format
import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1

fun Notification.toV1() = NotificationV1(
    id = this.id,
    title = this.title,
    body = this.body,
    createdAt = this.createdAt.format(),
    isVisualized = this.isVisualized
)