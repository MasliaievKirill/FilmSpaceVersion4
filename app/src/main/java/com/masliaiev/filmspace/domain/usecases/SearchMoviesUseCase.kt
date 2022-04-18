package com.masliaiev.filmspace.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun searchMovies(query: String): LiveData<PagingData<Movie>> {
        return repository.searchMovies(query)
    }
}