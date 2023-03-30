package com.kaiqkt.services.communicationservice.resources.springemail

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest

object SpringMailMock {

    lateinit var smtp: GreenMail

    fun start(login: String, password: String) {
        smtp = GreenMail(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser(login, password))
        smtp.start()
    }

    fun stop() = smtp.stop()
    fun reset() = smtp.reset()
}