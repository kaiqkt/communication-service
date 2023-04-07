package com.kaiqkt.services.communicationservice.resources.mongodb

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationHistoryRepositoryCustom
import com.kaiqkt.services.communicationservice.resources.exceptions.PersistenceException
import com.mongodb.client.result.UpdateResult
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class NotificationHistoryRepositoryImplTest {
    private val mongoTemplate: MongoTemplate = mockk(relaxed = true)
    private val repository: NotificationHistoryRepositoryCustom = NotificationHistoryRepositoryImpl(mongoTemplate)

    @Test
    fun `given a user and a notification, should persist successfully`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val history = NotificationHistorySampler.sample()
        val query = Query().addCriteria(Criteria.where("id").`is`(userId))

        every { mongoTemplate.findAndModify(any(), any(), any(), NotificationHistory::class.java) } returns history

        repository.insert(userId, notification)

        verify { mongoTemplate.findAndModify(query, any(), any(), NotificationHistory::class.java) }
    }

    @Test
    fun `given a user and a notification, when fail to persist, should throws a exception`() {
        val userId = ULID.random()
        val notification = NotificationSampler.sample()
        val query = Query().addCriteria(Criteria.where("id").`is`(userId))

        every { mongoTemplate.findAndModify(any(), any(), any(), NotificationHistory::class.java) } throws Exception()

        assertThrows<PersistenceException> {
            repository.insert(userId, notification)
        }

        verify { mongoTemplate.findAndModify(query, any(), any(), NotificationHistory::class.java) }
    }

    @Test
    fun `given a notification to update, should update successfully`() {
        val userId = ULID.random()
        val notificationId = ULID.random()
        val query = Query().addCriteria(Criteria.where("id").`is`(userId).and("notifications.id").`is`(notificationId))

        every {
            mongoTemplate.updateMulti(
                any(),
                any(),
                NotificationHistory::class.java
            )
        } returns UpdateResult.acknowledged(1, null, null)

        repository.updateNotification(userId, notificationId)

        verify { mongoTemplate.updateMulti(query, any(), NotificationHistory::class.java) }
    }

    @Test
    fun `given a notification to update, when fail, should throw a exception`() {
        val userId = ULID.random()
        val notificationId = ULID.random()
        val query = Query().addCriteria(Criteria.where("id").`is`(userId).and("notifications.id").`is`(notificationId))

        every { mongoTemplate.updateMulti(any(), any(), NotificationHistory::class.java) } throws Exception()

        assertThrows<PersistenceException> {
            repository.updateNotification(userId, notificationId)
        }

        verify { mongoTemplate.updateMulti(query, any(), NotificationHistory::class.java) }
    }
}