package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.Genre
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getGenres(): Pair<ResultParams, List<Genre>?> {
        return repository.getGenresList()
    }
}