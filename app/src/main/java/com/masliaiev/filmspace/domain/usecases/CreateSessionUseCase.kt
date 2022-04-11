package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class CreateSessionUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun createSession(requestToken: String): CreateSessionResponse {
        return repository.createSession(requestToken)
    }
}