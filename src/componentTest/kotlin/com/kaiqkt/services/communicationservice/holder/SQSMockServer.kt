package com.kaiqkt.services.communicationservice.holder

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazon.sqs.javamessaging.SQSSession
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.CreateQueueResult
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.amazonaws.services.sqs.model.QueueAttributeName
import org.elasticmq.rest.sqs.SQSRestServer
import org.elasticmq.rest.sqs.SQSRestServerBuilder
import javax.jms.Session

object SQSMockServer {

    private lateinit var server: SQSRestServer

    private val createQueueResulList: MutableList<CreateQueueResult> = mutableListOf()

    fun start(
        queues: List<String>,
        amazonSQSAsync: AmazonSQSAsync
    ) {
        server = SQSRestServerBuilder
            .withInterface("localhost")
            .withPort(4566)
            .start()
        server.waitUntilStarted()

        queues.forEach {
            val queue = amazonSQSAsync.createQueueAsync(
                CreateQueueRequest(it)
                    .addAttributesEntry(QueueAttributeName.VisibilityTimeout.name, "1")
            )
            if (queue.isDone) {
                createQueueResulList.add(queue.get())
            }
        }
    }

    fun stop() {
        server.stopAndWait()
    }

    fun reset(amazonSQSAsync: AmazonSQSAsync) {
        createQueueResulList.forEach {
            amazonSQSAsync.purgeQueueAsync(PurgeQueueRequest(it.queueUrl))
        }
    }

    private fun getSQSConnectionFactory(amazonSQSAsync: AmazonSQSAsync) = SQSConnectionFactory(ProviderConfiguration(), amazonSQSAsync).apply {
        createConnection().start()
    }

    fun getSQSSession(amazonSQSAsync: AmazonSQSAsync): Session =
        getSQSConnectionFactory(amazonSQSAsync).createConnection().createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE)

}