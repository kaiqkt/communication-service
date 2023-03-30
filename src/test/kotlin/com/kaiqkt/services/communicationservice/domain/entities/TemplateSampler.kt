package com.kaiqkt.services.communicationservice.domain.entities

object TemplateSampler {
    fun sample() = Template(
        url = "communication-d-1/welcome.html",
        data = mapOf("name" to "shinji")
    )

    fun pushTemplateSample() = Template(
        url = "communication-d-1/new-purchase.txt",
        data = mapOf()
    )

    fun smsTemplateSample() = Template(
        url = "communication-d-1/redefine-password.txt",
        data = mapOf("name" to "shinji")
    )

    fun invalidSample() = Template(
        url = "communication-d-1/redefin-password.txt",
        data = mapOf("name" to "shinji")
    )
}