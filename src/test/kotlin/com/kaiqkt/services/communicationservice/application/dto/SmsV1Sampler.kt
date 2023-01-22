package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.SmsV1

object SmsV1Sampler {
    fun sample() = SmsV1(
        recipient = "5511940028922",
        template = TemplateV1Sampler.sampleSmsTemplate()
    )
}