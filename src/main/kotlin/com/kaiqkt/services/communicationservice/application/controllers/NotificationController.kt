package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.commons.security.auth.AUTHORIZE_SERVICE
import com.kaiqkt.commons.security.auth.AUTHORIZE_USER
import com.kaiqkt.commons.security.auth.getUserId
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.application.dto.toV1
import com.kaiqkt.services.communicationservice.domain.services.NotificationService
import com.kaiqkt.services.communicationservice.generated.application.controllers.NotificationApi
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationHistoryV1
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(private val notificationService: NotificationService) : NotificationApi {

    @PreAuthorize(AUTHORIZE_SERVICE)
    override fun sendOne(userId: String, notificationV1: NotificationV1): ResponseEntity<Unit> {
        notificationService.send(userId, notificationV1.toDomain()).also {
            return ResponseEntity.noContent().build()
        }
    }

    @PreAuthorize(AUTHORIZE_USER)
    override fun find(): ResponseEntity<NotificationHistoryV1> {
        notificationService.find(getUserId())?.also {
            return ResponseEntity.ok(it.toV1())
        }
        return ResponseEntity.noContent().build()
    }
}