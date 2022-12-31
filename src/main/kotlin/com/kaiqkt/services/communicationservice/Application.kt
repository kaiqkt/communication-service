package com.kaiqkt.services.communicationservice

import com.kaiqkt.commons.health.COMMONS_HEALTH
import com.kaiqkt.commons.security.COMMONS_SECURITY
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.kaiqkt.services.communicationservice", COMMONS_SECURITY, COMMONS_HEALTH])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
