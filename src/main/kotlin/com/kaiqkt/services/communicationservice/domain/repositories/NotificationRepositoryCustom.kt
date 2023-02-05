package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.Notification

interface NotificationRepositoryCustom {
    fun insert(userId: String, notification: Notification)
}