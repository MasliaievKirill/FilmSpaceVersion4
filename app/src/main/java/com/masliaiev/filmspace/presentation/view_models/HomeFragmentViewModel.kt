package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.GetNowPlayingMoviesUseCase
import com.masliaiev.filmspace.domain.usecases.GetPopularMoviesUseCase
import com.masliaiev.filmspace.domain.usecases.GetTopRatedMoviesUseCase
import com.masliaiev.filmspace.domain.usecases.GetUpcomingMoviesUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    init {
        getNowPlaying()
        getUpcoming()
        getTopRated()
        getPopular()
    }

    private var _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>>
        get() = _nowPlayingMovies

    private var _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>>
        get() = _upcomingMovies

    private var _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies

    private var _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    private fun getNowPlaying() {
        viewModelScope.launch {
            val nowPlayingResult = getNowPlayingMoviesUseCase.getNowPlayingMovies()
            when (nowPlayingResult.first) {
                ResultParams.SUCCESS -> {
                    nowPlayingResult.second?.let {
                        _nowPlayingMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    private fun getUpcoming() {
        viewModelScope.launch {
            val upcomingResult = getUpcomingMoviesUseCase.getUpcomingMovies()
            when (upcomingResult.first) {
                ResultParams.SUCCESS -> {
                    upcomingResult.second?.let {
                        _upcomingMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    private fun getTopRated() {
        viewModelScope.launch {
            val topRatedResult = getTopRatedMoviesUseCase.getTopRatedMovies()
            when (topRatedResult.first) {
                ResultParams.SUCCESS -> {
                    topRatedResult.second?.let {
                        _topRatedMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    private fun getPopular() {
        viewModelScope.launch {
            val popularResult = getPopularMoviesUseCase.getPopularMovies()
            when (popularResult.first) {
                ResultParams.SUCCESS -> {
                    popularResult.second?.let {
                        _popularMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }


}