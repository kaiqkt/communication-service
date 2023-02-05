package com.kaiqkt.services.communicationservice

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationRepository
import com.kaiqkt.services.communicationservice.holder.S3MockServer
import com.kaiqkt.services.communicationservice.holder.SQSMockServer
import com.kaiqkt.services.communicationservice.resources.email.SpringMailMock
import com.kaiqkt.services.communicationservice.resources.sms.helpers.TwilioMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
class ApplicationIntegrationTest {

    @LocalServerPort
    val port: Int? = null

    @Value("\${twilio.account-sid}")
    lateinit var twilioAccountSid: String
    @Value("\${service-shared-secret}")
    lateinit var serviceSecret: String
    @Value("\${customer-auth-signing-secret}")
    lateinit var customerSecret: String
    @Value("\${aws.sqs.email-queue-name}")
    lateinit var emailQueue: String
    @Value("\${aws.sqs.sms-queue-name}")
    lateinit var smsQueue: String
    @Value("\${aws.region}")
    lateinit var region: String
    @Value("\${aws.s3.endpoint}")
    lateinit var s3Endpoint: String
    @Value("\${aws.access-key}")
    lateinit var accessKey: String
    @Value("\${aws.secret-key}")
    lateinit var secretKey: String
    @Value("\${spring.mail.password}")
    lateinit var smtpPassword: String
    @Value("\${spring.mail.username}")
    lateinit var smtpUsername: String
    @Autowired
    lateinit var amazonSQSAsync: AmazonSQSAsync
    @Autowired
    lateinit var notificationRepository: NotificationRepository

    lateinit var webSocketStompClient: WebSocketStompClient

    val classLoader: ClassLoader = javaClass.classLoader

    @BeforeAll
    fun beforeAll() {
        notificationRepository.deleteAll()
        SQSMockServer.start(listOf(emailQueue, smsQueue), amazonSQSAsync)
        S3MockServer.start(
            s3Endpoint,
            region,
            accessKey,
            secretKey
        )
        SpringMailMock.start(smtpUsername, smtpPassword)
        webSocketStompClient = WebSocketStompClient(
            SockJsClient(
                listOf<Transport>(WebSocketTransport(StandardWebSocketClient()))
            )
        )
    }

    @BeforeEach
    fun beforeEach() {
        SpringMailMock.reset()
        TwilioMock.reset()
        SQSMockServer.reset(amazonSQSAsync)
        S3MockServer.reset()
    }

    @AfterAll
    fun afterAll() {
        SQSMockServer.stop()
        S3MockServer.stop()
        SpringMailMock.stop()
    }

    @Autowired
    lateinit var webTestClient: WebTestClient
}