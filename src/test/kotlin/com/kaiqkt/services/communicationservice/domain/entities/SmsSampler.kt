package com.kaiqkt.services.communicationservice.domain.entities

object SmsSampler {
    fun sample() = Sms(
        recipient = "5511940028922",
        template = TemplateSampler.smsTemplateSample()
    )
}