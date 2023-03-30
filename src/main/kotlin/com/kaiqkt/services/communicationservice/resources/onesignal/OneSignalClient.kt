package com.kaiqkt.services.communicationservice.resources.onesignal

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isSuccessful
import com.kaiqkt.services.communicationservice.application.ext.ObjectMapperBuilder
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.onesignal.entities.PushRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class OneSignalClient(
    @Value("\${one-signal.url}")
    private val serviceUrl: String,
    @Value("\${one-signal.app-id}")
    private val appId: String,
    @Value("\${one-signal.auth-token}")
    private val authToken: String
) {
    fun sendPush(recipient: String, title: String, body: String) {
        logger.info("Sending push notification to $recipient")

        val request = PushRequest(
            appId = appId,
            headings = mapOf("en" to title),
            contents = mapOf("en" to body),
            channelForExternalUserIds = "push",
            includeExternalUserIds = listOf(recipient)
        )

        Fuel.post("$serviceUrl/api/v1/notifications")
            .header(
                mapOf(
                    Headers.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED,
                    Headers.AUTHORIZATION to "Bearer $authToken"
                )
            )
            .jsonBody(ObjectMapperBuilder.default.writeValueAsString(request))
            .response().let { (_, response, result) ->
                when {
                    response.isSuccessful -> {
                        logger.info("Push sent to: $recipient successfully")
                    }

                    else -> {
                        throw ResourceException(
                            "Failed to push notification for $recipient, status ${response.statusCode}, result: $result"
                        )
                    }
                }
            }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}