package com.kaiqkt.services.communicationservice.application.ext

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javax.jms.Message
import javax.jms.TextMessage

fun <T> Message.fromMessage(clazz: Class<T>): T {
    val textMessage: TextMessage = this as TextMessage
    val payload: String = textMessage.text
    return try {
        jacksonObjectMapper().readValue(payload, clazz)
    } catch (e: Exception) {
        throw e
    }
}