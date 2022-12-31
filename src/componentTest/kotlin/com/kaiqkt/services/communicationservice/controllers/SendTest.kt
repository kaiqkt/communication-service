package com.kaiqkt.services.communicationservice.controllers

import com.github.kittinunf.fuel.core.Headers
import com.kaiqkt.services.communicationservice.ApplicationIntegrationTest
import com.kaiqkt.services.communicationservice.application.dto.EmailV1Sampler
import com.kaiqkt.services.communicationservice.application.dto.SmsV1Sampler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class SendTest : ApplicationIntegrationTest() {
    @Test
    fun `given a send email request, when is send successfully, should return http status 204`() {
        val email = EmailV1Sampler.sample()

        webTestClient
            .post()
            .uri("/email")
            .contentType(MediaType.parseMediaType("application/vnd.kaiqkt_email_v1+json"))
            .header(Headers.AUTHORIZATION, serviceSecret)
            .bodyValue(email)
            .exchange()
            .expectStatus()
            .isNoContent

        val messages = amazonSQSAsync.receiveMessage(emailQueue).messages

        Assertions.assertEquals(1, messages.size)
    }

    @Test
    fun `given a send sms request, when is send successfully, should return http status 204`() {
        val sms = SmsV1Sampler.sample()

        webTestClient
            .post()
            .uri("/sms")
            .contentType(MediaType.parseMediaType("application/vnd.kaiqkt_sms_v1+json"))
            .header(Headers.AUTHORIZATION, serviceSecret)
            .bodyValue(sms)
            .exchange()
            .expectStatus()
            .isNoContent

        val messages = amazonSQSAsync.receiveMessage(smsQueue).messages

        Assertions.assertEquals(1, messages.size)
    }
}