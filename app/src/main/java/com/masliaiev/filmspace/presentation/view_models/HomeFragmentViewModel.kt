package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.GetNowPlayingMoviesUseCase
import com.masliaiev.filmspace.domain.usecases.GetTrendingDayUseCase
import com.masliaiev.filmspace.domain.usecases.GetTrendingWeekUseCase
import com.masliaiev.filmspace.domain.usecases.GetUpcomingMoviesUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getTrendingDayUseCase: GetTrendingDayUseCase,
    private val getTrendingWeekUseCase: GetTrendingWeekUseCase
) : ViewModel() {

    init {
        getNowPlaying()
        getUpcoming()
        getTrendingDay()
        getTrendingWeek()
    }

    private var _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>>
        get() = _nowPlayingMovies

    private var _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>>
        get() = _upcomingMovies

    private var _trendingDay = MutableLiveData<List<Movie>>()
    val trendingDay: LiveData<List<Movie>>
        get() = _trendingDay

    private var _trendingWeek = MutableLiveData<List<Movie>>()
    val trendingWeek: LiveData<List<Movie>>
        get() = _trendingWeek

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

    private fun getTrendingDay() {
        viewModelScope.launch {
            val topTrendingDayResult = getTrendingDayUseCase.getTrendingDay()
            when (topTrendingDayResult.first) {
                ResultParams.SUCCESS -> {
                    topTrendingDayResult.second?.let {
                        _trendingDay.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    private fun getTrendingWeek() {
        viewModelScope.launch {
            val trendingWeekResult = getTrendingWeekUseCase.getTrendingWeek()
            when (trendingWeekResult.first) {
                ResultParams.SUCCESS -> {
                    trendingWeekResult.second?.let {
                        _trendingWeek.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun tryAgain() {
        getNowPlaying()
        getUpcoming()
        getTrendingDay()
        getTrendingWeek()
    }

}