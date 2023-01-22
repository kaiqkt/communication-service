package com.kaiqkt.services.communicationservice.resources.sms

import com.kaiqkt.services.communicationservice.domain.entities.SmsSampler
import com.kaiqkt.services.communicationservice.domain.gateways.TwilioService
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.twilio.TwilioClient
import com.kaiqkt.services.communicationservice.resources.twilio.TwilioServiceImplementation
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TwilioServiceImplementationTest {
    private val twilioClient: TwilioClient = mockk(relaxed = true)
    private val twilioService: TwilioService = TwilioServiceImplementation(twilioClient)

    @Test
    fun `given sms request, should send successfully to twilio`() {
        val sms = SmsSampler.sample()

        every { twilioClient.sendSms(any(), any()) } just runs

        twilioService.send(sms.recipient, "hello world")

        verify { twilioClient.sendSms(sms.recipient, "hello world") }
    }

    @Test
    fun `given sms request, when fail to sent to twilio, should throw SendMessageException`() {
        val sms = SmsSampler.sample()

        every { twilioClient.sendSms(any(), any()) } throws ResourceException("error")

        assertThrows<ResourceException> {
            twilioService.send(sms.recipient, "hello world")
        }

        verify { twilioClient.sendSms(sms.recipient, "hello world") }

    }
}