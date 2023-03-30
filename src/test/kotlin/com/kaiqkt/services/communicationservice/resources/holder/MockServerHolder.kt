package com.kaiqkt.services.communicationservice.resources.holder

import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.verify.VerificationTimes
import kotlin.random.Random

abstract class MockServerHolder {

    companion object {
        private const val PORT: Int = 8081
        private val mockServer: ClientAndServer = ClientAndServer.startClientAndServer(PORT)
        private val baseUrl = "http://127.0.0.1:${mockServer.localPort}"
    }

    abstract fun domainPath(): String

    fun baseUrl() = "$baseUrl${domainPath()}"

    fun mockServer(): ClientAndServer = mockServer

    fun reset(): MockServerClient = mockServer.clear(HttpRequest.request().withPath("${domainPath()}/.*"))

    fun verifyRequest(httpRequest: HttpRequest, count: Int = 1) {
        mockServer.verify(
            httpRequest,
            VerificationTimes.exactly(count)
        )
    }
}