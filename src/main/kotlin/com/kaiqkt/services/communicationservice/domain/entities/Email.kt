package com.kaiqkt.services.communicationservice.domain.entities

data class Email(
    val subject: String,
    val recipient: String,
    val template: Template
)