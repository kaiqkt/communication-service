package com.kaiqkt.services.communicationservice.domain.gateways

interface MessagingService{
    fun send(message: Any, queueName: String)
}