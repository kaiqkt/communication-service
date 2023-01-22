package com.kaiqkt.services.communicationservice.domain.entities

data class Sms(
    val recipient: String,
    val template: Template
)