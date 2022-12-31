package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Email
import com.kaiqkt.services.communicationservice.generated.application.dto.EmailV1

fun EmailV1.toDomain() = Email(
    subject = this.subject,
    recipient = this.recipient,
    template = this.template.toDomain()
)