package com.kaiqkt.services.communicationservice.domain.entities

object TemplateFileSampler {
    fun sample() = TemplateFile(
        content = "<html>\${name}</html>"
    )
}