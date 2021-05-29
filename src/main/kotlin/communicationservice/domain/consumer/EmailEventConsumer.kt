package communicationservice.domain.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import communicationservice.domain.START_TOPIC
import communicationservice.domain.events.EmailEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class EmailEventConsumer(
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = [START_TOPIC])
    fun onMessage(consumerRecord: ConsumerRecord<String, String>) {
        val emailEvent = objectMapper.readValue(
            consumerRecord.value(),
            EmailEvent::class.java
        )
        val email = emailEvent.email
        logger.info(
            "Event consumed ${
                String(
                    consumerRecord.headers().first().value()
                )
            } id ${consumerRecord.key()} from topic ${consumerRecord.topic()} for event ${emailEvent.eventId}"
        )
    }
}