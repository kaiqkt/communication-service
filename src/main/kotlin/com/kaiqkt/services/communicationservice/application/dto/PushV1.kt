package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Push
import com.kaiqkt.services.communicationservice.generated.application.dto.PushV1

fun PushV1.toDomain() = Push(
    subject = this.subject,
    recipient = this.recipient,
    template = this.template.toDomain(),
    deepLink = this.deepLink
)