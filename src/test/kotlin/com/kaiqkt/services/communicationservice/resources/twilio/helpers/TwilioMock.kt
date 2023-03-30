package com.kaiqkt.services.communicationservice.resources.twilio.helpers

import com.kaiqkt.services.communicationservice.resources.holder.MockServerHolder

object TwilioMock: MockServerHolder() {


    val sendSms = SendSmsPathMock(this)
    override fun domainPath(): String = "/twilio"
}