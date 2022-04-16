package com.masliaiev.filmspace.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetAllFavouriteMoviesUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getAllFavouriteMovies(
        sessionId: String,
        accountId: Int
    ): LiveData<PagingData<Movie>> {
        return repository.getAllFavouriteMovies(sessionId, accountId)
    }
}