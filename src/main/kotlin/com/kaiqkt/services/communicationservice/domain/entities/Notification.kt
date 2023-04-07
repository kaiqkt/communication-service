package com.kaiqkt.services.communicationservice.domain.entities

import io.azam.ulidj.ULID
import java.time.LocalDateTime

data class Notification(
    val id: String = ULID.random(),
    val title: String,
    val body: String,
    val isVisualized: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)