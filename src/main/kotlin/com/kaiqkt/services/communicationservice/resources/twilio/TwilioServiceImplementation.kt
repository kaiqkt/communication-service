package com.kaiqkt.services.communicationservice.resources.twilio

import com.kaiqkt.services.communicationservice.domain.gateways.TwilioService
import org.springframework.stereotype.Component

@Component
class TwilioServiceImplementation(
    private val twilioClient: TwilioClient
) : TwilioService {

    override fun send(recipient: String, message: String) = twilioClient.sendSms(recipient, message)
}