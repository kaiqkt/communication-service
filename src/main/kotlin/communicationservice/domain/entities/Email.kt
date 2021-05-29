package communicationservice.domain.entities

data class Email(
    val subject: String,
    val to: String,
    val template: Template
)
