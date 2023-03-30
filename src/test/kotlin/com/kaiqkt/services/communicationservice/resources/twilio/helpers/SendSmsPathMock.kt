package com.kaiqkt.services.communicationservice.resources.twilio.helpers

import com.kaiqkt.services.communicationservice.resources.holder.MockServerHolder
import io.azam.ulidj.ULID
import com.github.kittinunf.fuel.core.Headers
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.http.MediaType

class SendSmsPathMock(private val holder: MockServerHolder) {
    private val mockServer = holder.mockServer()

    fun mockSendSms(accountSid: String) {
        mockServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withHeader(Headers.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .withPath("${holder.domainPath()}/Accounts/$accountSid/Messages.json")
        ).respond(
            HttpResponse.response()
                .withStatusCode(201)
        )
    }

    fun mockSendSmsError() {
        mockServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withHeader(Headers.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .withPath("${holder.domainPath()}/Accounts/${ULID.random()}/Messages.json")
        ).respond(
            HttpResponse.response()
                .withStatusCode(404)
        )
    }

    fun verifySendSms(accountSid: String, count: Int) = verify("${holder.domainPath()}/Accounts/$accountSid/Messages.json", count)

    private fun verify(path: String, count: Int) {
        val httpRequest = HttpRequest.request()
            .withMethod("POST")
            .withPath(path)
        holder.verifyRequest(httpRequest, count)
    }

}