package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.services.communicationservice.application.dto.SmsV1Sampler
import com.kaiqkt.services.communicationservice.domain.entities.Sms
import com.kaiqkt.services.communicationservice.domain.services.SmsService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus


class SmsControllerTest{
    private val smsService: SmsService = mockk(relaxed = true)
    private val smsController: SmsController = SmsController(smsService)

    @Test
    fun `given a request to send sms to sqs, when successfully should return http status 204`() {
        val request = SmsV1Sampler.sample()

        every { smsService.sendToQueue(any()) } just runs

        val response = smsController.sendOne(request)

        verify { smsService.sendToQueue(any()) }

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}