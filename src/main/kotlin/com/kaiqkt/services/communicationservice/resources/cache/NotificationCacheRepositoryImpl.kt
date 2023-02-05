package com.kaiqkt.services.communicationservice.resources.cache

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationCacheRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.PersistenceException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class NotificationCacheRepositoryImpl(private val redisTemplate: RedisTemplate<String, NotificationHistory>) :
    NotificationCacheRepository {
    private val hashOperations = redisTemplate.opsForValue()

    override fun insert(userId: String, notificationHistory: NotificationHistory) {
        try {
            hashOperations.set(userId, notificationHistory)
        } catch (ex: Exception) {
            throw PersistenceException(
                "Unable to insert notification history for $userId"
            )
        }
    }

    override fun findByUser(userId: String): NotificationHistory? {
        try {
            return hashOperations.get(userId)
        } catch (ex: Exception) {
            throw PersistenceException(
                "Unable to get notification history"
            )
        }
    }
}