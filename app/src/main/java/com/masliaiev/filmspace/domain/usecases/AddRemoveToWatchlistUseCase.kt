package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.responses.AddToWatchlistResponse
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class AddRemoveToWatchlistUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun addToWatchlist(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        watchlist: Boolean
    ): Pair<ResultParams, Boolean?> {
        return repository.addToWatchlist(
            accountId = accountId,
            movieId = movieId,
            watchlist = watchlist,
            sessionId = sessionId
        )
    }
}