package com.kaiqkt.services.communicationservice.resources.onesignal

import com.kaiqkt.services.communicationservice.domain.gateways.OneSignalService
import org.springframework.stereotype.Component

@Component
class OneSignalServiceImpl(
    private val oneSignalClient: OneSignalClient
): OneSignalService {

    override fun sendOne(recipient: String, title: String, body: String) {
        oneSignalClient.sendPush(recipient, title, body)
    }
}