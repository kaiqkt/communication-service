package com.kaiqkt.services.communicationservice.resources.twilio

import com.kaiqkt.services.communicationservice.domain.gateways.TwilioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TwilioServiceImplementation(
    private val twilioClient: TwilioClient
) : TwilioService {

    override fun send(phoneNumber: String, message: String) = twilioClient.sendSms(phoneNumber, message)
}