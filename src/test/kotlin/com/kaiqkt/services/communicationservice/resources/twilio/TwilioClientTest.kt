package com.kaiqkt.services.communicationservice.resources.twilio

import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.twilio.helpers.TwilioMock
import io.azam.ulidj.ULID
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TwilioClientTest {

    private val mockServer = TwilioMock

    private val accountSid: String = ULID.random()
    private val authToken: String = "secret"
    private val senderPhoneNumber: String = "+5501140028922"

    private val twilioClient: TwilioClient = TwilioClient(
        mockServer.baseUrl(),
        accountSid,
        authToken,
        senderPhoneNumber
    )

    @BeforeEach
    fun beforeEach() {
        mockServer.reset()
    }

    @Test
    fun `given the request to send an sms to twilio, should must be sent with successfully to twilio`() {
        val phoneNumber = "+5501140028923"
        val message = "Hello world"

        mockServer.sendSms.mockSendSms(accountSid)

        twilioClient.sendSms(phoneNumber, message)

        mockServer.sendSms.verifySendSms(accountSid, 1)
    }

    @Test
    fun `given the request to send an sms to twilio, when is properties in request, should throw SendMessageException and return http status 400`() {
        val phoneNumber = "+5501140028923"
        val message = "Hello world"
        mockServer.sendSms.mockSendSmsError()

        assertThrows<ResourceException> {
            twilioClient.sendSms(phoneNumber, message)
        }
    }
}