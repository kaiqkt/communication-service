package com.kaiqkt.services.communicationservice.domain.entities

object TemplateSampler {
    fun sample() = Template(
        url = "communication-d-1/welcome.html",
        data = mapOf("name" to "shinji")
    )

    fun smsSample() = Template(
        url = "communication-d-1/redefine-password.txt",
        data = mapOf("name" to "shinji")
    )

    fun smsInvalid() = Template(
        url = "communication-d-1/redefin-password.txt",
        data = mapOf("name" to "shinji")
    )
}