package com.masliaiev.filmspace.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetAllPopularMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getAllPopularMovies(): LiveData<PagingData<Movie>> {
        return repository.getAllPopularMovies()
    }
}