package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory

interface NotificationRepositoryCustom {
    fun insert(userId: String, notification: Notification): NotificationHistory?
}