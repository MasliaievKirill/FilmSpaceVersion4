package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getPopularMovies(): Pair<ResultParams, List<Movie>?> {
        return repository.getPopularMovies()
    }
}