package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.domain.entities.Phone
import com.kaiqkt.services.communicationservice.generated.application.dto.PhoneV1

fun PhoneV1.toDomain() = Phone(
    countryCode = this.countryCode,
    areaCode = this.areaCode,
    phoneNumber = this.phoneNumber
)