package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.SearchMoviesUseCase
import javax.inject.Inject

class SearchFragmentViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    var movies: LiveData<PagingData<Movie>>? = null

    fun searchMovies(query: String) {
        val moviesResult = searchMoviesUseCase.searchMovies(query).cachedIn(viewModelScope)
        movies = moviesResult
    }

    fun setError() {
        _error.value = true
    }

    fun clearError() {
        _error.value = false
    }
}