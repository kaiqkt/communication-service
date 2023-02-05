package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory

interface NotificationCacheRepository {
    fun insert(userId: String, notificationHistory: NotificationHistory)
    fun findByUser(userId: String): NotificationHistory?
}