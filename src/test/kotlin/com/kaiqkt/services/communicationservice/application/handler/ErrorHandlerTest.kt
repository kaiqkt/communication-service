package com.kaiqkt.services.communicationservice.application.handler

import com.kaiqkt.services.communicationservice.application.dto.ErrorSampler
import com.kaiqkt.services.communicationservice.domain.exceptions.DomainException
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest

class ErrorHandlerTest{

    private val webRequest: WebRequest = mockk(relaxed = true)

    @Test
    fun `given an error handler when handling a DomainException should return HTTP status 400`() {
        val errorHandler = ErrorHandler()
        val error = ErrorSampler.sample()
        val resultBindingException = DomainException(error.details.toString())

        val response = errorHandler.handleDomainException(resultBindingException, webRequest)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals(error.details, response.body?.details)
    }
}