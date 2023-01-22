package com.kaiqkt.services.communicationservice.domain.gateways

interface TwilioService {
    fun send(recipient: String, message: String)
}