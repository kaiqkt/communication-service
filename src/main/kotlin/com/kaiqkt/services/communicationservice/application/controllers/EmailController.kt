package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.commons.security.auth.AUTHORIZE_SERVICE
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.domain.services.EmailService
import com.kaiqkt.services.communicationservice.generated.application.controllers.EmailApi
import com.kaiqkt.services.communicationservice.generated.application.dto.EmailV1
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController(
    private val emailService: EmailService
) : EmailApi {

    @PreAuthorize(AUTHORIZE_SERVICE)
    override fun sendOne(emailV1: EmailV1): ResponseEntity<Unit> {
        emailService.sendToQueue(emailV1.toDomain())
        return ResponseEntity.noContent().build()
    }
}