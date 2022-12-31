package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.PhoneV1

object PhoneV1Sampler {
    fun sample() = PhoneV1(
        countryCode = "55",
        areaCode = "11",
        phoneNumber = "940028922"
    )
}