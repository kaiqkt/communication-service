package com.kaiqkt.services.communicationservice.resources.websocket

import com.kaiqkt.services.communicationservice.domain.entities.Session
import org.springframework.web.socket.CloseStatus
import java.time.LocalDateTime
import java.time.ZoneId


object WebsocketSessionHolder {
    private val store = HashMap<String, MutableList<Session>>()

    fun save(userId: String, session: Session) {
        synchronized(store) {
            val sessions = store[userId] ?: mutableListOf()
            sessions.add(session)
            store.put(userId, sessions)
        }
    }

    fun remove(webSocketSessionId: String) {
        synchronized(store) {
            val data = store.entries
            data.map {
                val sessions = it.value.iterator()

                while (sessions.hasNext()) {
                    val session = sessions.next()

                    if (session.webSocketSession.id == webSocketSessionId) {
                        sessions.remove()
                    }
                }
            }
        }
    }

    fun validateAndClose(userId: String): Boolean {
        synchronized(store) {
            val sessions = store[userId]

            if (sessions.isNullOrEmpty()) {
                return false
            }

            val it = sessions.iterator()

            while (it.hasNext()) {
                val session = it.next()
                val now = LocalDateTime.now()
                val expiration = session.expireAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                if (now.isAfter(expiration)) {
                    session.webSocketSession.close(CloseStatus.POLICY_VIOLATION)
                    it.remove()
                    store[userId] = sessions
                }
            }
        }

        return true
    }
}