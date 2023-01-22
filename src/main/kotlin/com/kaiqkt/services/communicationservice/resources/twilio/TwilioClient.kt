package com.kaiqkt.services.communicationservice.resources.twilio

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.cUrlString
import com.github.kittinunf.fuel.core.isSuccessful
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class TwilioClient(
    @Value("\${twilio.url}")
    private val serviceUrl: String,
    @Value("\${twilio.account-sid}")
    private val accountSid: String,
    @Value("\${twilio.auth-token}")
    private val authToken: String,
    @Value("\${twilio.sender-phone-number}")
    private val senderPhoneNumber: String
) {
    fun sendSms(recipient: String, message: String) {
        logger.info("Sending sms message to $recipient")

        val body = "Body=$message&From=$senderPhoneNumber&To=$recipient"
        Fuel.post("$serviceUrl/Accounts/$accountSid/Messages.json")
            .authentication()
            .basic(accountSid, authToken)
            .header(
                mapOf(
                    Headers.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED
                )
            )
            .body(body)
            .response().let { (_, response, result) ->
                when {
                    response.isSuccessful -> {
                        logger.info("Sms sent to: $recipient successfully")
                    }

                    else -> {
                        throw ResourceException(
                            "Failed to send sms for $recipient, status ${response.statusCode}, result: $result"
                        )
                    }
                }
            }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

}