package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.Notification

interface NotificationHistoryRepositoryCustom {
    fun insert(userId: String, notification: Notification)
    fun updateNotification(userId: String, notificationId: String)
}