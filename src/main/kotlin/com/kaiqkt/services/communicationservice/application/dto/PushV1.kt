package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Push
import com.kaiqkt.services.communicationservice.generated.application.dto.PushV1

fun PushV1.toDomain() = Push(
    title = this.title,
    recipient = this.recipient,
    template = this.template.toDomain()
)