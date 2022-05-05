package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getSimilarMovies(movieId: Int): Pair<ResultParams, List<Movie>?> {
        return repository.getSimilarMovies(movieId)
    }
}