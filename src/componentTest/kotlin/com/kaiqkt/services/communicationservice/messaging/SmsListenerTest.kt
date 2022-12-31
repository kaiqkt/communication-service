package com.kaiqkt.services.communicationservice.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.application.messaging.SmsListener
import com.kaiqkt.services.communicationservice.domain.entities.SmsSampler
import com.kaiqkt.services.communicationservice.holder.S3MockServer
import com.kaiqkt.services.communicationservice.holder.SQSMockServer
import com.kaiqkt.services.communicationservice.resources.sms.helpers.TwilioMock
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class SmsListenerTest : ApplicationIntegrationTest() {

    @Autowired
    private lateinit var smsListener: SmsListener

    private val filePath = "redefine-password.txt"

    @Test
    fun `given a send sms message, when the message arrives at the queue, should be consumed with successfully and get template in s3 and send sms`() {
        TwilioMock.sendSms.mockSendSms(twilioAccountSid)

        val sms = SmsSampler.sample().run { jacksonObjectMapper().writeValueAsString(this) }
        val message = SQSMockServer.getSQSSession(amazonSQSAsync).createTextMessage(sms)
        uploadFile()

        smsListener.onMessage(message)

        TwilioMock.sendSms.verifySendSms(twilioAccountSid, 1)
    }

    fun uploadFile() {
        val staticFile = classLoader.getResource(filePath)!!.readText()

        S3MockServer.put(filePath, staticFile, "text/plain")
    }
}