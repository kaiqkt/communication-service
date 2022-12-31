package com.kaiqkt.services.communicationservice.resources.aws.s3

import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class TemplateFileRepositoryImplementationTest {
    private val amazonS3: AmazonS3 = mockk(relaxed = true)
    private val s3Object: S3Object = mockk(relaxed = true)
    private val bucketName = "communication-d-1/sms"
    private val fileName = "redefine-password.txt"
    private val templateFileRepository: TemplateFileRepository = TemplateFileRepositoryImplementation(amazonS3)

    @Test
    fun `given a file, when bucket name not found, should throw a exception`() {
        every {
            amazonS3.getObject(bucketName, fileName)
        } throws SdkClientException("Bucket not found")

        assertThrows<ResourceException> { templateFileRepository.find("communication-d-1/sms/redefine-password.txt") }

        verify { amazonS3.getObject(bucketName, fileName) }
    }

    @Test
    fun `given a file,when is retrieved with successfully, should fill result`() {
        mockFile()

        templateFileRepository.find("communication-d-1/sms/redefine-password.txt")

        verify { amazonS3.getObject(bucketName, fileName) }
        verify { s3Object.objectContent }
    }

    private fun mockFile() {
        val fileContent = this::class.java.getResourceAsStream("/txt/redefine-password.txt")?.bufferedReader()?.readText()
        val stream = fileContent?.byteInputStream()
        val s3ObjectInputStream = S3ObjectInputStream(stream, null)

        every { amazonS3.getObject(bucketName, fileName) } returns s3Object
        every { s3Object.objectContent } returns s3ObjectInputStream
    }
}