package com.kaiqkt.services.communicationservice.domain.gateways

import com.kaiqkt.services.communicationservice.domain.entities.Notification

interface WebSocketService {
    fun sendNotification (userId: String, notification: Notification)
}