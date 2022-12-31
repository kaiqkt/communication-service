package com.kaiqkt.services.communicationservice.domain.entities

object SmsSampler {
    fun sample() = Sms(
        phone = PhoneSampler.sample(),
        template = TemplateSampler.smsSample()
    )
}