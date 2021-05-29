package communicationservice.application.dto

import communicationservice.domain.entities.Push

data class PushRequest(
    var title: String,
    val message: String,
    val payload: Map<String, String>,
    val topic: String,
    val token: String
)

fun PushRequest.toDomain() = Push(
    title = this.title,
    message = this.message,
    payload = this.payload,
    topic = this.topic,
    token = this.token,
)