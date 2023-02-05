package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.SessionSampler
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

class WebsocketSessionHolderTest {

    private val webSocketSession: WebSocketSession = mockk(relaxed = true)
    private val holder = WebsocketSessionHolder

    @Test
    fun `given a user id and a session, when exist user in hash map should just add in the existent list and save successfully`() {
        val userId = ULID.random()
        val session = SessionSampler.sample(webSocketSession)

        holder.save(userId, session)

        Assertions.assertTrue(holder.validateAndClose(userId))
    }

    @Test
    fun `given a user id, when exist sessions and is not expired, should validate successfully and not close any session`() {
        val userId = ULID.random()
        val session = SessionSampler.sample(webSocketSession)

        every { webSocketSession.close(any()) } just runs

        holder.save(userId, session)

        val hasSession = holder.validateAndClose(userId)

        Assertions.assertTrue(hasSession)
    }

    @Test
    fun `given a user id, when exist sessions and is  expired, should validate successfully and close the expired session`() {
        val userId = ULID.random()
        val expiredSession = SessionSampler.expiredSample(webSocketSession)
        val session = SessionSampler.sample(webSocketSession)

        every { webSocketSession.close(any()) } just runs

        holder.save(userId, session)
        holder.save(userId, expiredSession)

        val hasSession = holder.validateAndClose(userId)

        Assertions.assertTrue(hasSession)

        verify(exactly = 1) { webSocketSession.close(CloseStatus.POLICY_VIOLATION) }
    }

    @Test
    fun `given a user id, when no exist sessions `() {
        val userId = ULID.random()

        val hasSession = holder.validateAndClose(userId)

        Assertions.assertFalse(hasSession)
    }

    @Test
    fun `given web socket session id, should delete`() {
        val userId = ULID.random()
        val session = SessionSampler.sample(webSocketSession)

        holder.save(userId, session)

        val hasSession = holder.validateAndClose(userId)

        Assertions.assertTrue(hasSession)

        holder.remove(webSocketSession.id)

        val hasSessionAfterDelete = holder.validateAndClose(userId)

        Assertions.assertFalse(hasSessionAfterDelete)
    }

    @Test
    fun `given web socket session id, when not exist, should do nothing`() {
        val userId = ULID.random()
        val session = SessionSampler.sample(webSocketSession)

        holder.save(userId, session)

        val hasSession = holder.validateAndClose(userId)

        Assertions.assertTrue(hasSession)

        holder.remove(UUID.randomUUID().toString())

        val hasSessionAfterDelete = holder.validateAndClose(userId)

        Assertions.assertTrue(hasSessionAfterDelete)
    }
}