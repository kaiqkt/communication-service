package com.kaiqkt.services.communicationservice.resources.onesignal

import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.onesignal.helpers.OneSignalMock
import io.azam.ulidj.ULID
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class OneSignalClientTest{
    private val mockServer = OneSignalMock

    private val appId = UUID.randomUUID().toString()
    private val authToken = "token"

    private val client = OneSignalClient(
        mockServer.baseUrl(),
        appId,
        authToken
    )

    @BeforeEach
    fun beforeEach() {
        mockServer.reset()
    }

    @Test
    fun `given a push to send, should send successfully`(){
        val recipient = ULID.random()
        val title = "PUSH"
        val body = "Push"

        mockServer.sendPush.mockSendPush()

        client.sendPush(recipient, title, body, mapOf())

        mockServer.sendPush.verifySendPush(1)
    }

    @Test
    fun `given a push to send, when the deep link is null, should send successfully`(){
        val recipient = ULID.random()
        val title = "PUSH"
        val body = "Push"

        mockServer.sendPush.mockSendPush()

        client.sendPush(recipient, title, body, mapOf())

        mockServer.sendPush.verifySendPush(1)
    }


    @Test
    fun `given a push to send, when occur a error, should send successfully`(){
        val recipient = ULID.random()
        val title = "PUSH"
        val body = "Push"

        mockServer.sendPush.mockSendPushError()

        assertThrows<ResourceException> {
            client.sendPush(recipient, title, body, mapOf())
        }

        mockServer.sendPush.verifySendPush(1)
    }
}