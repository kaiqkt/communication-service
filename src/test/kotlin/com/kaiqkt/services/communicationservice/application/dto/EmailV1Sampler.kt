package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.EmailV1

object EmailV1Sampler {
    fun sample() = EmailV1(
        subject = "test",
        recipient = "shinji@gmail.com",
        template = TemplateV1Sampler.sampleEmailTemplate()
    )
}