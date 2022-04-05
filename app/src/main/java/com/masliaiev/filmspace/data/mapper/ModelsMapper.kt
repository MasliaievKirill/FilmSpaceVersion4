package com.masliaiev.filmspace.data.mapper

import com.masliaiev.filmspace.data.network.models.requests.CreateSessionRequestDto
import com.masliaiev.filmspace.data.network.models.requests.DeleteSessionRequestDto
import com.masliaiev.filmspace.data.network.models.responses.CreateRequestTokenResponseDto
import com.masliaiev.filmspace.data.network.models.responses.CreateSessionResponseDto
import com.masliaiev.filmspace.data.network.models.responses.DeleteSessionResponseDto
import com.masliaiev.filmspace.domain.entity.requests.CreateSessionRequest
import com.masliaiev.filmspace.domain.entity.requests.DeleteSessionRequest
import com.masliaiev.filmspace.domain.entity.responses.CreateRequestTokenResponse
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.entity.responses.DeleteSessionResponse
import javax.inject.Inject

class ModelsMapper @Inject constructor() {

    fun mapCreateRequestTokenResponseDtoToCreateRequestTokenResponseEntity(
        createRequestTokenResponseDto: CreateRequestTokenResponseDto
    ): CreateRequestTokenResponse {
        return CreateRequestTokenResponse(
            success = createRequestTokenResponseDto.success,
            expiresAt = createRequestTokenResponseDto.expiresAt,
            requestToken = createRequestTokenResponseDto.requestToken
        )
    }

    fun mapCreateSessionRequestEntityToCreateSessionRequestDto(
        createSessionRequest: CreateSessionRequest
    ): CreateSessionRequestDto {
        return CreateSessionRequestDto(
            requestToken = createSessionRequest.requestToken
        )
    }

    fun mapCreateSessionResponseDtoToCreateSessionResponseEntity(
        createSessionResponseDto: CreateSessionResponseDto
    ) : CreateSessionResponse {
        return CreateSessionResponse(
            success = createSessionResponseDto.success,
            sessionId = createSessionResponseDto.sessionId
        )
    }

    fun mapDeleteSessionRequestEntityToDeleteSessionRequestDto(
        deleteSessionRequest: DeleteSessionRequest
    ) : DeleteSessionRequestDto {
        return DeleteSessionRequestDto(
            sessionId = deleteSessionRequest.sessionId
        )
    }

    fun mapDeleteSessionResponseDtoToDeleteSessionResponseEntity(
        deleteSessionResponseDto: DeleteSessionResponseDto
    ): DeleteSessionResponse {
        return DeleteSessionResponse(
            success = deleteSessionResponseDto.success
        )
    }


}