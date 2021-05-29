package communicationservice.application.controllers

import communicationservice.application.dto.EmailRequest
import communicationservice.application.dto.toDomain
import communicationservice.domain.START_TOPIC
import communicationservice.domain.events.EmailEvent
import communicationservice.domain.producer.EmailEventProducer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController(
    private val eventProducer: EmailEventProducer
) {

    @PostMapping("/send/email")
    fun send(@RequestBody request: EmailRequest): ResponseEntity<HttpStatus> {
        val event = EmailEvent.create(request.toDomain())
        eventProducer.send(event, START_TOPIC)

        return ResponseEntity(HttpStatus.OK)
    }
}