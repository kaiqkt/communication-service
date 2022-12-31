package com.kaiqkt.services.communicationservice.domain.gateways

interface TwilioService {
    fun send(phoneNumber: String, message: String)
}