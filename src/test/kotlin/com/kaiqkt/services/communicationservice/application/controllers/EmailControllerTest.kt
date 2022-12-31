package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.services.communicationservice.application.dto.EmailV1Sampler
import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.domain.services.EmailService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class EmailControllerTest{
    private val emailService: EmailService = mockk(relaxed = true)
    private val emailController: EmailController = EmailController(emailService)

    @Test
    fun `given a request to send email to sqs, when successfully should return http status 204`() {
        val request = EmailV1Sampler.sample()

        every { emailService.sendToQueue(any()) } just runs

        val response = emailController.sendOne(request)

        verify { emailService.sendToQueue(any()) }

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}