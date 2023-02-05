package com.kaiqkt.services.communicationservice.domain.entities

import io.azam.ulidj.ULID

object NotificationHistorySampler {
    fun sample() = NotificationHistory(
        id = ULID.random(),
        notifications = listOf(NotificationSampler.sample())
    )
}