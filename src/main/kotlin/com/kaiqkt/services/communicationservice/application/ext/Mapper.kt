package com.kaiqkt.services.communicationservice.application.ext

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper =  jacksonObjectMapper().registerModule(JavaTimeModule())