package communicationservice.application.dto

import communicationservice.domain.entities.Email

data class EmailRequest(
    val subject: String,
    val to: String,
    val templateRequest: TemplateRequest
)

fun EmailRequest.toDomain() = Email(
    subject = this.subject,
    to = this.to,
    template = this.templateRequest.toDomain()
)


