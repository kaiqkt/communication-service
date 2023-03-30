package com.kaiqkt.services.communicationservice.resources.onesignal.helpers

import com.kaiqkt.services.communicationservice.resources.holder.MockServerHolder

object OneSignalMock: MockServerHolder() {

    override fun domainPath(): String = "/one-signal"

    val sendPush = SendPushPathMock(this)
}