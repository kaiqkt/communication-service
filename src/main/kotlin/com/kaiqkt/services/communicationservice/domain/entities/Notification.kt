package com.kaiqkt.services.communicationservice.domain.entities

import java.time.LocalDateTime

data class Notification(
    val title: String,
    val body: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)