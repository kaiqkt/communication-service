package com.kaiqkt.services.communicationservice.application.messaging

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.kaiqkt.services.communicationservice.application.ext.fromMessage
import com.kaiqkt.services.communicationservice.domain.entities.Sms
import com.kaiqkt.services.communicationservice.domain.services.SmsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import javax.jms.Message

@Component
class SmsListener(
    private val smsService: SmsService
) {

    @JmsListener(destination = "\${aws.sqs.sms-queue-name}")
    fun onMessage(message: Message) {
            logger.info("Receiving sms message")
            val sms = message.fromMessage(Sms::class.java)
            smsService.send(sms)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}