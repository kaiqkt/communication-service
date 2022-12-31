package com.kaiqkt.services.communicationservice.resources.aws.sqs

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AmazonSQSConfig(
    @Value("\${aws.access-key}")
    private val accessKey: String,
    @Value("\${aws.secret-key}")
    private val secretKey: String,
    @Value("\${aws.region}")
    private val awsRegion: String,
    @Value("\${aws.sqs.endpoint}")
    private val messageBrokerEndpoint: String,
) {

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync? {
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        return AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    messageBrokerEndpoint,
                    awsRegion
                )
            )
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }

    @Bean
    fun connectionFactory(amazonSQSAsync: AmazonSQSAsync): SQSConnectionFactory =
        SQSConnectionFactory(ProviderConfiguration(), amazonSQSAsync)
}