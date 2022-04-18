package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.DetailedMovie
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetDetailedMovieUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getDetailedMovie(movieId: Int): Pair<ResultParams, DetailedMovie?> {
        return repository.getDetailedMovie(movieId)
    }
}