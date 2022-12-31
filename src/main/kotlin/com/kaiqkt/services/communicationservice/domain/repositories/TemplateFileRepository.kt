package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.TemplateFile

interface TemplateFileRepository {
    fun find(url: String): TemplateFile
}