package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class MarkAsFavouriteUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun markAsFavourite(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        favourite: Boolean
    ): Pair<ResultParams, Boolean?> {
        return repository.markAsFavourite(
            accountId = accountId,
            movieId = movieId,
            favourite = favourite,
            sessionId = sessionId
        )
    }
}