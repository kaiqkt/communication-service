package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationHistoryV1

fun NotificationHistory.toV1() = NotificationHistoryV1(
    notifications = this.notifications.map { it.toV1() }
)