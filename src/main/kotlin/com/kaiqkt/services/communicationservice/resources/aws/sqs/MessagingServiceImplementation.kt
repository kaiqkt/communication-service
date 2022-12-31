package com.kaiqkt.services.communicationservice.resources.aws.sqs

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.domain.entities.Sms
import com.kaiqkt.services.communicationservice.domain.gateways.MessagingService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.exceptions.UnexpectedException
import io.azam.ulidj.ULID
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class MessagingServiceImplementation(
    private val amazonSQSAsync: AmazonSQSAsync
) : MessagingService {

    override fun send(message: Any, queueName: String) {
        logger.info("Sending message request to queue $queueName")

        val messageRequest = SendMessageRequest().apply {
            withQueueUrl(queueName)
            withMessageBody(jacksonObjectMapper().writeValueAsString(message))
        }

        try {
            amazonSQSAsync.sendMessage(messageRequest)
            logger.info("Sent message request successfully to queue $queueName")
        } catch (ex: Exception) {
            throw ResourceException("Error sending message to queue $queueName")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

}