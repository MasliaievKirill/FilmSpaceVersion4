package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class LoadAccountUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun loadAccountDetails(sessionId: String): ResultParams {
        return repository.loadAccountDetails(sessionId)
    }
}