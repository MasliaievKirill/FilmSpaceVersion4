package com.masliaiev.filmspace.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetAllTopRatedMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getAllTopRatedMovies(): LiveData<PagingData<Movie>> {
        return repository.getAllTopRatedMovies()
    }
}