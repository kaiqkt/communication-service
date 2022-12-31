package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.commons.security.auth.AUTHORIZE_SERVICE
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.domain.services.SmsService
import com.kaiqkt.services.communicationservice.generated.application.controllers.SmsApi
import com.kaiqkt.services.communicationservice.generated.application.dto.SmsV1
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class SmsController(private val smsService: SmsService): SmsApi {

    @PreAuthorize(AUTHORIZE_SERVICE)
    override fun sendOne(smsV1: SmsV1): ResponseEntity<Unit> {
        smsService.sendToQueue(smsV1.toDomain())
        return ResponseEntity.noContent().build()
    }
}