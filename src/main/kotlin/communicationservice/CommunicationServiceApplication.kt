package communicationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommunicationServiceApplication

fun main(args: Array<String>) {
	runApplication<CommunicationServiceApplication>(*args)
}
