package com.kaiqkt.services.communicationservice.resources.onesignal

import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OneSignalServiceImplTest{
    private val oneSignalClient: OneSignalClient = mockk(relaxed = true)
    private val serviceImpl = OneSignalServiceImpl(oneSignalClient)

    @Test
    fun `given a push, should send successfully`(){
        val recipient = ULID.random()
        val title = "Title"
        val body = "Body"

        every { oneSignalClient.sendPush(any(), any(), any()) } just runs

        serviceImpl.sendOne(recipient, title, body)

        verify { oneSignalClient.sendPush(recipient, title, body) }
    }

    @Test
    fun `given a push, when occur a error, should throw a exception`(){
        val recipient = ULID.random()
        val title = "Title"
        val body = "Body"

        every { oneSignalClient.sendPush(any(), any(), any()) } throws ResourceException("")

        assertThrows<ResourceException> {
            serviceImpl.sendOne(recipient, title, body)
        }

        verify { oneSignalClient.sendPush(recipient, title, body) }
    }
}