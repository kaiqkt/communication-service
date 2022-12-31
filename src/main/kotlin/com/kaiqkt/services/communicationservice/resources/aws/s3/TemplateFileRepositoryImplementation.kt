package com.kaiqkt.services.communicationservice.resources.aws.s3

import com.amazonaws.services.s3.AmazonS3
import com.kaiqkt.services.communicationservice.domain.entities.TemplateFile
import com.kaiqkt.services.communicationservice.domain.repositories.TemplateFileRepository
import com.kaiqkt.services.communicationservice.resources.exceptions.ResourceException
import com.kaiqkt.services.communicationservice.resources.exceptions.UnexpectedException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TemplateFileRepositoryImplementation(
    private val amazonS3: AmazonS3
) : TemplateFileRepository {

    override fun find(url: String): TemplateFile {
        val bucketName = getBucketName(url)
        val fileKey = getFileKey(url)

        logger.info("Retrieving file $fileKey from $bucketName")
        val s3Object = try {
            amazonS3.getObject(bucketName, fileKey)
        }catch (ex: Exception){
            throw ResourceException("Unable to retrieve file $fileKey from $bucketName")
        }

        return TemplateFile(String(s3Object.objectContent.readBytes()))
    }

    private fun getBucketName(url: String): String =
        url.substring(url.indexOf(url.first()), url.lastIndexOf("/"))

    private fun getFileKey(url: String) = url.split("/").last()

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}