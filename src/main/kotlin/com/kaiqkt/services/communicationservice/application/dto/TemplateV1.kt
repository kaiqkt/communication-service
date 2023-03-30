package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.TemplateV1
import com.kaiqkt.services.communicationservice.domain.entities.Template

fun TemplateV1.toDomain() = Template(
    url = this.url,
    data = this.data ?: mapOf()
)