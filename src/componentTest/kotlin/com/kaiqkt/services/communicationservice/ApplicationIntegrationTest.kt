package com.kaiqkt.services.communicationservice

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.kaiqkt.services.communicationservice.domain.repositories.NotificationHistoryRepository
import com.kaiqkt.services.communicationservice.holder.S3MockServer
import com.kaiqkt.services.communicationservice.holder.SQSMockServer
import com.kaiqkt.services.communicationservice.resources.springemail.SpringMailMock
import com.kaiqkt.services.communicationservice.resources.twilio.helpers.TwilioMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
class ApplicationIntegrationTest {

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

    @Value("\${aws.sqs.push-queue-name}")
    lateinit var pushQueue: String

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
    lateinit var notificationHistoryRepository: NotificationHistoryRepository

    val classLoader: ClassLoader = javaClass.classLoader

    @BeforeAll
    fun beforeAll() {
        notificationHistoryRepository.deleteAll()
        SQSMockServer.start(listOf(emailQueue, smsQueue, pushQueue), amazonSQSAsync)
        S3MockServer.start(
            s3Endpoint,
            region,
            accessKey,
            secretKey
        )
        SpringMailMock.start(smtpUsername, smtpPassword)
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