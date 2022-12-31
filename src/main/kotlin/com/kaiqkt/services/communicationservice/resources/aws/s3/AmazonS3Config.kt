package com.kaiqkt.services.communicationservice.resources.aws.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonS3Config(
    @Value("\${aws.access-key}")
    private val accessKey: String,
    @Value("\${aws.secret-key}")
    private val secretKey: String,
    @Value("\${aws.region}")
    private val awsRegion: String,
    @Value("\${aws.s3.endpoint}")
    private val templateBucketEndpoint: String
) {

    @Bean
    fun amazonS3(): AmazonS3 {
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        return AmazonS3ClientBuilder
            .standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    templateBucketEndpoint,
                    awsRegion
                )
            )
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }
}