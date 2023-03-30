package com.kaiqkt.services.communicationservice.application.controllers

import com.kaiqkt.commons.security.auth.getUserId
import com.kaiqkt.services.communicationservice.application.dto.toDomain
import com.kaiqkt.services.communicationservice.application.dto.toV1
import com.kaiqkt.services.communicationservice.domain.services.PushService
import com.kaiqkt.services.communicationservice.generated.application.controllers.PushApi
import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationHistoryV1
import com.kaiqkt.services.communicationservice.generated.application.dto.PushV1
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PushController(
    private val pushService: PushService
) : PushApi {

    override fun sendOne(pushV1: PushV1): ResponseEntity<Unit> {
        pushService.sendToQueue(pushV1.toDomain()).also {
            return ResponseEntity.noContent().build()
        }
    }

    override fun findAll(): ResponseEntity<NotificationHistoryV1> {
        pushService.findNotificationHistory(getUserId())?.let {
            return ResponseEntity.ok(it.toV1())
        } ?: return ResponseEntity.notFound().build()
    }
}