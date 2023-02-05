package com.kaiqkt.services.communicationservice.resources.mongodb

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.entities.NotificationSampler
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepositoryCustom
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class NotificationRepositoryImplTest{
    private val mongoTemplate: MongoTemplate = mockk(relaxed = true)
    private val repository: NotificationRepositoryCustom = NotificationRepositoryImpl(mongoTemplate)

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
}