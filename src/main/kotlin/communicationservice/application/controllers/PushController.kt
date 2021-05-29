package communicationservice.application.controllers

import communicationservice.application.dto.PushRequest
import communicationservice.application.dto.toDomain
import communicationservice.domain.services.PushService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PushController(
    private val pushService: PushService
) {

    @PostMapping("/send/push")
    fun sendNotification(@RequestBody request: PushRequest): ResponseEntity<HttpStatus> {
        pushService.sendPushNotificationWithoutData(request.toDomain())
        return ResponseEntity(HttpStatus.OK)
    }
}