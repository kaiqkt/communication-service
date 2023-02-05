package com.kaiqkt.services.communicationservice.resources.mongodb

import com.kaiqkt.services.communicationservice.domain.entities.Notification
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepositoryCustom
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NotificationRepositoryImpl(private val mongoTemplate: MongoTemplate) : NotificationRepositoryCustom {

    override fun insert(userId: String, notification: Notification) {
        val query = Query().addCriteria(Criteria.where("id").`is`(userId))

        val update = Update().apply {
            this.push("notifications", notification)
            this.set("updatedAt", LocalDateTime.now())
        }

        mongoTemplate.upsert(query, update, NotificationHistory::class.java)
    }
}