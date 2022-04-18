package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.AccountStates
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetAccountStateUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getAccountStates(
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, AccountStates?> {
        return repository.getAccountStates(movieId, sessionId)
    }
}