package com.kaiqkt.services.communicationservice.application.messaging

import com.kaiqkt.services.communicationservice.application.ext.fromMessage
import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.domain.services.EmailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import javax.jms.Message


@Component
class EmailListener(
    private val emailService: EmailService
) {

    @JmsListener(destination = "\${aws.sqs.email-queue-name}")
    fun onMessage(message: Message) {
        logger.info("Receiving email message")

        val email = message.fromMessage(Email::class.java)
        emailService.send(email)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}