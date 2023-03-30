package com.kaiqkt.services.communicationservice.domain.gateways

interface OneSignalService {
    fun sendOne(recipient: String, title: String, body: String)
}