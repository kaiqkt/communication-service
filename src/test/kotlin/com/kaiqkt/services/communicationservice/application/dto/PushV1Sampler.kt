package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.PushV1
import io.azam.ulidj.ULID

object PushV1Sampler {
    fun sample() = PushV1(
        subject = "push",
        recipient = ULID.random(),
        template = TemplateV1Sampler.samplePushTemplate()
    )
}