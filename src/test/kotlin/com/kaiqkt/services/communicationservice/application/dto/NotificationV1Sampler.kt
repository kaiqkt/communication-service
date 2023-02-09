package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.NotificationV1

object NotificationV1Sampler {
    fun sample() = NotificationV1(
        title = "title",
        body = "body"
    )
}