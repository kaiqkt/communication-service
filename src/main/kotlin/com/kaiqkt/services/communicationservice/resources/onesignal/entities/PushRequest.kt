package com.kaiqkt.services.communicationservice.resources.onesignal.entities

data class PushRequest(
    val appId: String,
    val headings: Map<String, String>,
    val contents: Map<String, String>,
    val channelForExternalUserIds: String,
    val includeExternalUserIds: List<String>,
    val data: Map<String, String?>
)
