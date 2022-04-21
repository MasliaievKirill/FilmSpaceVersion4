package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class DeleteRatingUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun deleteRating(
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, Boolean?> {
        return repository.deleteRating(movieId, sessionId)
    }
}