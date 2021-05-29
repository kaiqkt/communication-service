package communicationservice.resources.config

import communicationservice.domain.START_TOPIC
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder

@Configuration
@EnableKafka
class KafkaConfig {
    @Bean
    fun libraryEvents(): NewTopic {
        return TopicBuilder.name(START_TOPIC)
            .partitions(3)
            .replicas(1)
            .build()
    }
}