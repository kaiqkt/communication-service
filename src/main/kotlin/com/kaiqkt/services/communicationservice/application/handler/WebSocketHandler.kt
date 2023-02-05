package com.kaiqkt.services.communicationservice.application.handler

import com.kaiqkt.commons.crypto.jwt.JWTUtils
import com.kaiqkt.commons.security.auth.filter.BEARER_PREFIX
import com.kaiqkt.services.communicationservice.domain.entities.Session
import com.kaiqkt.services.communicationservice.resources.websocket.WebsocketSessionHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory


@Component
class WebSocketHandler(
    @Value("\${customer-auth-signing-secret}")
    private val secret: String
) {
    fun factory(): WebSocketHandlerDecoratorFactory {
        return WebSocketHandlerDecoratorFactory { webSocketHandler ->
            object : WebSocketHandlerDecorator(webSocketHandler) {

                @Throws(Exception::class)
                override fun afterConnectionEstablished(session: WebSocketSession) {
                    try {
                        val token = session.handshakeHeaders["Authorization"]?.first()?.replace(BEARER_PREFIX, "")
                        if (token != null) {
                            val authentication = JWTUtils.getClaims(token, secret)

                            Session(webSocketSession = session, expireAt = authentication.expireAt).run {
                                WebsocketSessionHolder.save(authentication.id, this)
                                log.info("Web socket session saved successfully for user ${authentication.id} in holder")
                            }
                        } else {
                            throw AccessDeniedException("Bearer token not passed")
                        }
                    } catch (e: Exception) {
                        session.close(CloseStatus.POLICY_VIOLATION) }

                    super.afterConnectionEstablished(session)
                }

                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {

                    try {
                        WebsocketSessionHolder.remove(session.id)
                        log.info("Web socket session ${session.id} removed from holder")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    super.afterConnectionClosed(session, closeStatus)
                }

            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }
}