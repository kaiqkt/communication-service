package com.kaiqkt.services.communicationservice.holder

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3ObjectSummary
import io.findify.s3mock.S3Mock

object S3MockServer {

    private const val BUCKET_NAME = "communication-d-1"

    private lateinit var server: S3Mock
    private lateinit var client: AmazonS3

    fun start(
        endpoint: String,
        region: String,
        accessKey: String,
        secretKey: String
    ) {
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        server = S3Mock.Builder()
            .withPort(4567)
            .withInMemoryBackend()
            .build().apply {
                this.start()
            }
        client = client(endpoint, region, credentials)
    }

    private fun client(
        endpoint: String,
        region: String,
        credentials: BasicAWSCredentials
    ): AmazonS3 {

        return AmazonS3ClientBuilder
            .standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    endpoint,
                    region
                )
            )
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build().apply {
                createBucket(BUCKET_NAME)
            }
    }

    fun put(fileKey: String, content: String, contentType: String) {
        val metadata = ObjectMetadata().apply {
            this.contentEncoding = "base64"
            this.contentType = contentType
        }
        client.putObject(BUCKET_NAME, fileKey, content.byteInputStream(), metadata)
    }

    fun reset() {
        var objectListing: ObjectListing = client.listObjects(BUCKET_NAME)
        while (true) {
            val iterator: Iterator<S3ObjectSummary> = objectListing.objectSummaries.iterator()
            while (iterator.hasNext()) {
                client.deleteObject(BUCKET_NAME, iterator.next().key)
            }

            objectListing = if (objectListing.isTruncated) {
                client.listNextBatchOfObjects(objectListing)
            } else {
                break
            }
        }
    }

    fun stop() {
        server.shutdown()
    }
}