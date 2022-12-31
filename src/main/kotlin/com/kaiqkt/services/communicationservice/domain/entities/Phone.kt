package com.kaiqkt.services.communicationservice.domain.entities

data class Phone(
    var countryCode: String,
    var areaCode: String,
    var phoneNumber: String
) {
    val completeNumber = "$countryCode$areaCode$phoneNumber"
}