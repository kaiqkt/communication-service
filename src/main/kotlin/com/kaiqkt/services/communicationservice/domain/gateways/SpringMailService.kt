package com.kaiqkt.services.communicationservice.domain.gateways

interface SpringMailService {
    fun send(recipient: String, subject: String, template: String)
}