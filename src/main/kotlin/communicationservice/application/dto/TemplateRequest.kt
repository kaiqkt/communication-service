package communicationservice.application.dto

import communicationservice.domain.entities.Template

data class TemplateRequest(
    val path: String,
    val data: Map<String, String>
)

fun TemplateRequest.toDomain() = Template(
    path = this.path,
    data = this.data
)