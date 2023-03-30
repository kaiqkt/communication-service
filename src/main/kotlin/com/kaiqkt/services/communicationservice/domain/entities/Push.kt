package com.kaiqkt.services.communicationservice.domain.entities

data class Push(
    val title: String,
    val recipient: String,
    val template: Template
)
