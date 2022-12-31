package com.kaiqkt.services.communicationservice.application.dto

import com.kaiqkt.services.communicationservice.generated.application.dto.ErrorResponseV1

object ErrorSampler {
    fun sample() = ErrorResponseV1(
        details = "error"
    )
}