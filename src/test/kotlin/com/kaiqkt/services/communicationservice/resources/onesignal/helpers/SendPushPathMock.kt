package com.kaiqkt.services.communicationservice.resources.onesignal.helpers

import com.kaiqkt.services.communicationservice.resources.holder.MockServerHolder
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

class SendPushPathMock(private val holder: MockServerHolder) {
    private val mockServer = holder.mockServer()

    fun mockSendPush() {
        mockServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("${holder.domainPath()}/api/v1/notifications")
        ).respond(
            HttpResponse.response()
                .withStatusCode(200)
        )
    }

    fun mockSendPushError() {
        mockServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("${holder.domainPath()}/api/v1/notifications")
        ).respond(
            HttpResponse.response()
                .withStatusCode(404)
        )
    }

    fun verifySendPush(count: Int) = verify(count)

    private fun verify(count: Int) {
        val httpRequest = HttpRequest.request()
            .withMethod("POST")
            .withPath("${holder.domainPath()}/api/v1/notifications")
        holder.verifyRequest(httpRequest, count)
    }
}