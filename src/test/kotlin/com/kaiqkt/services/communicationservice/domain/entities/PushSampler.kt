package com.kaiqkt.services.communicationservice.domain.entities

import io.azam.ulidj.ULID

object PushSampler {
    fun sample() = Push(
        subject = "push",
        recipient = ULID.random(),
        template = TemplateSampler.pushTemplateSample(),
        deepLink = "Home"
    )
}