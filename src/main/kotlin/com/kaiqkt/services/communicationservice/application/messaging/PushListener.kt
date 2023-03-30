package com.kaiqkt.services.communicationservice.application.messaging

import com.kaiqkt.services.communicationservice.application.ext.fromMessage
import com.kaiqkt.services.communicationservice.domain.entities.Push
import com.kaiqkt.services.communicationservice.domain.services.PushService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import javax.jms.Message

@Component
class PushListener(private val pushService: PushService) {

    @JmsListener(destination = "\${aws.sqs.push-queue-name}")
    fun onMessage(message: Message) {
        logger.info("Receiving push message")

        val push = message.fromMessage(Push::class.java)
        pushService.send(push)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
