package communicationservice.domain.services

import com.google.firebase.messaging.*
import communicationservice.domain.entities.Push
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ExecutionException
import communicationservice.domain.entities.Notification

@Service
class FirebaseService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(InterruptedException::class, ExecutionException::class)
    fun sendMessage(data: Map<String, String>, request: Push) {
        val message: Message = getPreconfiguredMessageWithData(data, request)
        val response: String = sendAndGetResponse(message)
        logger.info("Sent message with data. Topic: " + request.topic + ", " + response)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun sendMessageWithoutData(request: Push) {
        val message: Message = getPreconfiguredMessageWithoutData(request)
        val response: String = sendAndGetResponse(message)
        logger.info("Sent message without data. Topic: " + request.topic + ", " + response)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    fun sendMessageToToken(request: Push) {
        val message: Message = getPreconfiguredMessageToToken(request)
        val response: String = sendAndGetResponse(message)
        logger.info("Sent message to token. Device token: " + request.token + ", " + response)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun sendAndGetResponse(message: Message?): String {
        return FirebaseMessaging.getInstance().sendAsync(message).get()
    }

    private fun getAndroidConfig(topic: String): AndroidConfig {
        return AndroidConfig.builder()
            .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(
                AndroidNotification.builder().setSound(Notification.SOUND.value)
                    .setColor(Notification.COLOR.value).setTag(topic).build()
            ).build()
    }

    private fun getApnsConfig(topic: String): ApnsConfig {
        return ApnsConfig.builder()
            .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build()
    }

    private fun getPreconfiguredMessageToToken(request: Push): Message {
        return getPreconfiguredMessageBuilder(request).setToken(request.topic)
            .build()
    }

    private fun getPreconfiguredMessageWithoutData(request: Push): Message {
        return getPreconfiguredMessageBuilder(request).setTopic(request.topic)
            .build()
    }

    private fun getPreconfiguredMessageWithData(data: Map<String, String>, request: Push): Message{
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.topic)
            .build()
    }

    private fun getPreconfiguredMessageBuilder(request: Push): Message.Builder {
        val androidConfig = getAndroidConfig(request.topic)
        val apnsConfig = getApnsConfig(request.topic)
        return Message.builder()
            .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                Notification(request.title, request.message)
            )
    }
}