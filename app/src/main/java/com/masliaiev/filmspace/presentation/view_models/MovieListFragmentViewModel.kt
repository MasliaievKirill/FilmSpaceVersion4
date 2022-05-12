package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.*
import javax.inject.Inject

class MovieListFragmentViewModel @Inject constructor(
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val getAllMoviesWatchlistUseCase: GetAllMoviesWatchlistUseCase,
    private val getAllNowPlayingMoviesUseCase: GetAllNowPlayingMoviesUseCase,
    private val getAllPopularMoviesUseCase: GetAllPopularMoviesUseCase,
    private val getAllRatedMoviesUseCase: GetAllRatedMoviesUseCase,
    private val getAllTopRatedMoviesUseCase: GetAllTopRatedMoviesUseCase,
    private val getAllUpcomingMoviesUseCase: GetAllUpcomingMoviesUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val getAllTrendingDayUseCase: GetAllTrendingDayUseCase,
    private val getAllTrendingWeekUseCase: GetAllTrendingWeekUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase
) : ViewModel() {

    var movies: LiveData<PagingData<Movie>>? = null

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    fun getAllTopRatedMovies() {
        movies = getAllTopRatedMoviesUseCase.getAllTopRatedMovies().cachedIn(viewModelScope)
    }

    fun getAllNowPlayingMovies() {
        movies = getAllNowPlayingMoviesUseCase.getAllNowPlayingMovies().cachedIn(viewModelScope)
    }

    fun getAllPopularMovies() {
        movies = getAllPopularMoviesUseCase.getAllPopularMovies().cachedIn(viewModelScope)
    }

    fun getAllUpcomingMovies() {
        movies = getAllUpcomingMoviesUseCase.getAllUpcomingMovies().cachedIn(viewModelScope)
    }

    fun getMoviesByGenre(genre: String) {
        movies = getMoviesByGenreUseCase.getMoviesByGenre(genre).cachedIn(viewModelScope)
    }

    fun getTrendingDayMovies() {
        movies = getAllTrendingDayUseCase.getAllTrendingDayMovies().cachedIn(viewModelScope)
    }

    fun getTrendingWeekMovies() {
        movies = getAllTrendingWeekUseCase.getAllTrendingWeekMovies().cachedIn(viewModelScope)
    }

    suspend fun getAllFavouriteMovies() {
        movies = getAllFavouriteMoviesUseCase.getAllFavouriteMovies(
            getSessionIdUseCase.getSessionId(),
            getAccountDetailsUseCase.getAccountDetails().id
        ).cachedIn(viewModelScope)
    }

    suspend fun getAllRatedMovies() {
        movies = getAllRatedMoviesUseCase.getAllRatedMovies(
            getSessionIdUseCase.getSessionId(),
            getAccountDetailsUseCase.getAccountDetails().id
        ).cachedIn(viewModelScope)
    }

    suspend fun getAllMoviesWatchlist() {
        movies = getAllMoviesWatchlistUseCase.getAllMoviesWatchlist(
            getSessionIdUseCase.getSessionId(),
            getAccountDetailsUseCase.getAccountDetails().id
        ).cachedIn(viewModelScope)
    }

}