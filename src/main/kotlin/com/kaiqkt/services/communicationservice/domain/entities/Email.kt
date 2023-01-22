package com.kaiqkt.services.communicationservice.domain.entities

import kotlin.String

data class Email(
    val subject: String,
    val recipient: String,
    val template: Template
)