package communicationservice.domain.services

import communicationservice.domain.entities.Push
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException

@Service
class PushService(
    private val firebaseService: FirebaseService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

//    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
//    fun sendSamplePushNotification() {
//        try {
//            firebaseService.sendMessageWithoutData(getSamplePushNotificationRequest())
//        } catch (e: InterruptedException) {
//            logger.error(e.message)
//        } catch (e: ExecutionException) {
//            logger.error(e.message)
//        }
//    }

    fun sendPushNotification(request: Push) {
        try {
            firebaseService.sendMessage(request.payload, request)
        } catch (e: InterruptedException) {
            logger.error(e.message)
        } catch (e: ExecutionException) {
            logger.error(e.message)
        }
    }

    fun sendPushNotificationWithoutData(request: Push) {
        try {
            firebaseService.sendMessageWithoutData(request)
        } catch (e: InterruptedException) {
            logger.error(e.message)
        } catch (e: ExecutionException) {
            logger.error(e.message)
        }
    }


    fun sendPushNotificationToToken(request: Push) {
        try {
            firebaseService.sendMessageToToken(request)
        } catch (e: InterruptedException) {
            logger.error(e.message)
        } catch (e: ExecutionException) {
            logger.error(e.message)
        }
    }
}