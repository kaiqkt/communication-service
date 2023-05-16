package com.kaiqkt.services.communicationservice.domain.entities

data class Push(
    val subject: String,
    val recipient: String,
    val template: Template,
    val deepLink: String? = null
)
