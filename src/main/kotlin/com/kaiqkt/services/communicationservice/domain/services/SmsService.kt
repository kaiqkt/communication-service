package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.Sms
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.TwilioService
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import org.apache.commons.text.StringSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SmsService(
    private val twilioService: TwilioService,
    private val templateFileRepository: TemplateFileRepository,
    private val messagingService: MessagingService,
    @Value("\${aws.sqs.sms-queue-name}")
    private val queueName: String
) {

    fun sendToQueue(sms: Sms) = messagingService.send(sms, queueName)

    fun send(sms: Sms) {
        try {
            val templateFile = templateFileRepository.find(sms.template.url)
            val renderedTemplate = StringSubstitutor(sms.template.data).replace(templateFile.content)

            twilioService.send(sms.recipient, renderedTemplate)
        } catch (ex: Exception) {
            logger.error("Unable to send sms, error ${ex.message}")
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}