package com.kaiqkt.services.communicationservice.application.handler

import com.kaiqkt.services.communicationservice.domain.exceptions.DomainException
import com.kaiqkt.services.communicationservice.generated.application.dto.ErrorResponseV1
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException, request: WebRequest): ResponseEntity<ErrorResponseV1> {
        val responseBody = ErrorResponseV1(ex.message)

        log.error("Error: ${getUri(request)}")

        return ResponseEntity(responseBody, HttpStatus.BAD_REQUEST)
    }

    private fun getUri(request: WebRequest): List<String> = request.getDescription(true).split(";")

}