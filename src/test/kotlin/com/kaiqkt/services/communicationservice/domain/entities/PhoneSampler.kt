package com.kaiqkt.services.communicationservice.domain.entities

object PhoneSampler {

    fun sample() = Phone(
        countryCode = "55",
        areaCode = "011",
        phoneNumber = "914562121"
    )
}