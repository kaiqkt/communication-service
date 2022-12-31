package com.kaiqkt.services.communicationservice.resources.sms.helpers

import com.kaiqkt.services.communicationservice.resources.holder.MockServerHolder

object TwilioMock: MockServerHolder() {
    val sendSms = SendSmsPathMock(this)
}