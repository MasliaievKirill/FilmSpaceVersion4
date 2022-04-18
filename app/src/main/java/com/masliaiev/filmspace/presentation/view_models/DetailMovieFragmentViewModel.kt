package com.masliaiev.filmspace.presentation.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.AccountStates
import com.masliaiev.filmspace.domain.entity.DetailedMovie
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.*
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailMovieFragmentViewModel @Inject constructor(
    private val getAccountStateUseCase: GetAccountStateUseCase,
    private val getDetailedMovieUseCase: GetDetailedMovieUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
) : ViewModel() {


    private var _recommendations = MutableLiveData<List<Movie>>()
    val recommendations: LiveData<List<Movie>>
        get() = _recommendations

    private var _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>>
        get() = _similarMovies

    private var _accountState = MutableLiveData<AccountStates>()
    val accountState: LiveData<AccountStates>
        get() = _accountState

    private var _detailedMovie = MutableLiveData<DetailedMovie>()
    val detailedMovie: LiveData<DetailedMovie>
        get() = _detailedMovie

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    fun getAccountState(movieId: Int) {
        viewModelScope.launch {
            Log.d("Session", getSessionIdUseCase.getSessionId())
            val result = getAccountStateUseCase.getAccountStates(movieId, getSessionIdUseCase.getSessionId())
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _accountState.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun getDetailedMovie(movieId: Int) {
        viewModelScope.launch {
            val result = getDetailedMovieUseCase.getDetailedMovie(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _detailedMovie.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }


    fun getRecommendations(movieId: Int) {
        viewModelScope.launch {
            val result = getRecommendationsUseCase.getRecommendations(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _recommendations.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            val result = getSimilarMoviesUseCase.getSimilarMovies(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _similarMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }






}