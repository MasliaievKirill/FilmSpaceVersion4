package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class DeleteSessionUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun deleteSession(
        sessionId: String
    ): Pair<ResultParams, Boolean?> {
        return repository.deleteSession(sessionId)
    }
}