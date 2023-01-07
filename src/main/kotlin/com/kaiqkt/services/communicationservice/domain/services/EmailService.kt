package com.kaiqkt.services.communicationservice.domain.services

import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.domain.gateways.SpringMailService
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import org.apache.commons.text.StringSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val springMailService: SpringMailService,
    private val templateFileRepository: TemplateFileRepository,
    @Value("\${aws.sqs.email-queue-name}")
    private val queueName: String,
    private val messagingService: MessagingService
) {

    fun sendToQueue(email: Email) = messagingService.send(email, queueName)

    fun send(email: Email) {
        try {
            val templateFile = templateFileRepository.find(email.template.url)
            val renderedTemplate = StringSubstitutor(email.template.data).replace(templateFile.content)
            springMailService.send(email.recipient, email.subject, renderedTemplate)
        } catch (ex: Exception) {
            logger.info("Unable to send email, error: {${ex.message}}")
        }

    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}