package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.responses.CreateRequestTokenResponse
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class CreateRequestTokenUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun createRequestToken(): Pair<ResultParams, CreateRequestTokenResponse?> {
        return repository.createRequestToken()
    }
}