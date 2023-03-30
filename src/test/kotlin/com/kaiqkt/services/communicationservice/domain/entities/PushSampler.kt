package com.kaiqkt.services.communicationservice.domain.entities

import io.azam.ulidj.ULID

object PushSampler {
    fun sample() = Push(
        title = "push",
        recipient = ULID.random(),
        template = TemplateSampler.pushTemplateSample()
    )
}