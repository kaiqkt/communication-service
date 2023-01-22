package com.kaiqkt.services.communicationservice.domain.entities

import kotlin.String

data class Template(
    val url: String,
    val data: Map<String, String>
)