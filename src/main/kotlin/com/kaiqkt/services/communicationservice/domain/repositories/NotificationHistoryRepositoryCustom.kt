package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.Notification

interface NotificationHistoryRepositoryCustom {
    fun insert(userId: String, notification: Notification)
}