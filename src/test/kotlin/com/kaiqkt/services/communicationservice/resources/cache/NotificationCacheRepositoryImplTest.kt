package com.kaiqkt.services.communicationservice.resources.cache

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistorySampler
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationCacheRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.PersistenceException
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.redis.core.RedisTemplate

class NotificationCacheRepositoryImplTest{
    private val redisTemplate: RedisTemplate<String, NotificationHistory> = mockk(relaxed = true)
    private val hashOperations = redisTemplate.opsForValue()
    private val repository: NotificationCacheRepository = NotificationCacheRepositoryImpl(redisTemplate)

    @Test
    fun `given a user id and a notification history should insert successfully`() {
        val history = NotificationHistorySampler.sample()

        every { hashOperations.set(any(),any()) } just runs

        repository.insert(history.id, history)

        verify { hashOperations.set(history.id, history) }
    }

    @Test
    fun `given a user id and a notification history, when fail to insert, should throw PersistenceException`() {
        val history = NotificationHistorySampler.sample()

        every { hashOperations.set(any(),any()) } throws Exception()

        assertThrows<PersistenceException> {
            repository.insert(history.id, history)
        }

        verify { hashOperations.set(history.id, history) }
    }

    @Test
    fun `given a user id, exist notification history, should return an history`() {
        val history = NotificationHistorySampler.sample()

        every { hashOperations.get(any()) } returns history

        repository.findByUser(history.id)


        verify { hashOperations.get(history.id) }
    }

    @Test
    fun `given a user id, when fail to find, should throw PersistenceException`() {
        val userId = ULID.random()

        every { hashOperations.get(any()) } throws Exception()

        assertThrows<PersistenceException> {
            repository.findByUser(userId)
        }

        verify { hashOperations.get(userId) }
    }
}