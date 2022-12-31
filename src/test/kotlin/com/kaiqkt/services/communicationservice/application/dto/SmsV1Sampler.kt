package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.SmsV1

object SmsV1Sampler {
    fun sample() = SmsV1(
        phone = PhoneV1Sampler.sample(),
        template = TemplateV1Sampler.sampleSmsTemplate()
    )
}