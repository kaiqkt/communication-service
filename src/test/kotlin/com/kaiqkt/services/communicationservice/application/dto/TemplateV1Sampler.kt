package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.TemplateV1

object TemplateV1Sampler {
    fun sampleSmsTemplate() = TemplateV1(
        url = "communication-sqs-d-1/sms/welcome-sms-template.txt",
        data = emptyMap()
    )

    fun sampleEmailTemplate() = TemplateV1(
        url = "communication-sqs-d-1/emails/welcome-email-template.html",
        data = emptyMap()
    )
}