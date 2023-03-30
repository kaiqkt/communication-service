package com.kaiqkt.services.communicationservice.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.application.messaging.PushListener
import com.kaiqkt.services.communicationservice.domain.entities.PushSampler
import com.kaiqkt.services.communicationservice.domain.entities.TemplateSampler
import com.kaiqkt.services.communicationservice.holder.S3MockServer
import com.kaiqkt.services.communicationservice.holder.SQSMockServer
import com.kaiqkt.services.communicationservice.resources.onesignal.helpers.OneSignalMock
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PushListenerTest : ApplicationIntegrationTest() {

    @Autowired
    private lateinit var pushListener: PushListener

    private val filePath = "new-purchase.txt"

    @Test
    fun `given a send push message, when the message arrives at the queue, should be consumed with successfully and get template in s3 and send push`() {
        OneSignalMock.sendPush.mockSendPush()

        val push = PushSampler.sample().run { jacksonObjectMapper().writeValueAsString(this) }
        val message = SQSMockServer.getSQSSession(amazonSQSAsync).createTextMessage(push)

        uploadFile()

        pushListener.onMessage(message)

        OneSignalMock.sendPush.verifySendPush(1)
    }

    @Test
    fun `given a send email message, when fail, should do nothing`() {
        OneSignalMock.sendPush.mockSendPush()

        val push = PushSampler.sample().copy(template = TemplateSampler.invalidSample())
            .run { jacksonObjectMapper().writeValueAsString(this) }
        val message = SQSMockServer.getSQSSession(amazonSQSAsync).createTextMessage(push)

        uploadFile()

        pushListener.onMessage(message)

        OneSignalMock.sendPush.verifySendPush(0)
    }

    private fun uploadFile() {
        val staticFile = classLoader.getResource(filePath)!!.readText()

        S3MockServer.put(filePath, staticFile, "text/html")
    }
}