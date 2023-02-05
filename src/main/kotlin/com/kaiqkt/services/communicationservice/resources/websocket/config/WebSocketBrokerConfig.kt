package com.kaiqkt.services.communicationservice.resources.websocket.config

import com.kaiqkt.services.communicationservice.application.handler.WebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration


@Configuration
@EnableWebSocketMessageBroker
class WebSocketBrokerConfig(
    private val webSocketHandler: WebSocketHandler
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/user")
        config.setUserDestinationPrefix("/user")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
        registry.addEndpoint("/ws").withSockJS()
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        registry.addDecoratorFactory(webSocketHandler.factory())
    }
}

@Configuration
class WebSocketSecurityConfig : AbstractSecurityWebSocketMessageBrokerConfigurer() {
    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry) {
        messages.simpTypeMatchers(SimpMessageType.MESSAGE).denyAll()
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }

}