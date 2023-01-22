 package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Sms
import com.kaiqkt.services.communicationservice.generated.application.dto.SmsV1

fun SmsV1.toDomain() = Sms(
    recipient = this.recipient,
    template = this.template.toDomain()
)