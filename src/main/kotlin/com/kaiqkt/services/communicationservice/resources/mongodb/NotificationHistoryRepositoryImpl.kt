package com.kaiqkt.services.communicationservice.resources.mongodb

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationHistoryRepositoryCustom
import com.kaiqkt.services.communicationservice.resources.exceptions.PersistenceException
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NotificationHistoryRepositoryImpl(private val mongoTemplate: MongoTemplate) : NotificationHistoryRepositoryCustom {

    override fun insert(userId: String, notification: Notification) {
        try {
            val query = Query().addCriteria(Criteria.where("id").`is`(userId))

            val update = Update().apply {
                this.push("notifications", notification)
                this.set("updatedAt", LocalDateTime.now())
            }

            mongoTemplate.findAndModify(query, update, FindAndModifyOptions().upsert(true), NotificationHistory::class.java)
        }catch (ex: Exception) {
            throw PersistenceException("Error inserting new notification to user $userId}, Error: $ex")
        }
    }

    override fun updateNotification(userId: String, notificationId: String) {
        try {
            val query = Query().addCriteria(Criteria.where("id").`is`(userId).and("notifications.id").`is`(notificationId))

            val update = Update().apply {
                this.set("notifications.$.isVisualized", true)
                this.set("updatedAt", LocalDateTime.now())
            }

            mongoTemplate.updateMulti(query, update, NotificationHistory::class.java)
        }catch (ex: Exception) {
            throw PersistenceException("Error updating notification $notificationId to user $userId}, Error: $ex")
        }
    }
}