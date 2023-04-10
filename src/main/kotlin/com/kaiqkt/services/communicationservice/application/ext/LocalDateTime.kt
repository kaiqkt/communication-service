package com.kaiqkt.services.communicationservice.application.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.format(): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    return formatter.format(this)
}