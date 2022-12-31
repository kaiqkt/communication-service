package com.kaiqkt.services.communicationservice.domain.entities

object EmailSampler {
    fun sample() = Email(
        subject = "email",
        recipient = "shjinjiikari@hot.com",
        template = TemplateSampler.sample()
    )
}