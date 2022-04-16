package com.masliaiev.filmspace.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getMoviesByGenre(
        genre: String
    ): LiveData<PagingData<Movie>> {
        return repository.getMoviesByGenre(genre)
    }
}