package com.kaiqkt.services.communicationservice.messaging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.application.messaging.EmailListener
import com.kaiqkt.services.communicationservice.domain.entities.EmailSampler
import com.kaiqkt.services.communicationservice.holder.S3MockServer
import com.kaiqkt.services.communicationservice.holder.SQSMockServer
import com.kaiqkt.services.communicationservice.resources.email.SpringMailMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EmailListenerTest : ApplicationIntegrationTest() {

    @Autowired
    private lateinit var emailListener: EmailListener

    private val filePath = "welcome.html"

    @Test
    fun `given a send email message, when the message arrives at the queue, should be consumed with successfully and get template in s3 and send email`() {
        val email = EmailSampler.sample().run { jacksonObjectMapper().writeValueAsString(this) }
        val message = SQSMockServer.getSQSSession(amazonSQSAsync).createTextMessage(email)

        uploadFile()
        emailListener.onMessage(message)

        Assertions.assertEquals(1, SpringMailMock.smtp.receivedMessages.size)
    }

    private fun uploadFile() {
        val staticFile = classLoader.getResource(filePath)!!.readText()

        S3MockServer.put(filePath, staticFile, "text/html")
    }

}