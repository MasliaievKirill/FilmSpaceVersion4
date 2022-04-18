package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.*
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
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
    private val loadAccountUseCase: LoadAccountUseCase,
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
        val moviesResult = getAllTopRatedMoviesUseCase.getAllTopRatedMovies()
        movies = moviesResult
    }

    fun getAllNowPlayingMovies() {
        val moviesResult = getAllNowPlayingMoviesUseCase.getAllNowPlayingMovies()
        movies = moviesResult
    }

    fun getAllPopularMovies() {
        val moviesResult = getAllPopularMoviesUseCase.getAllPopularMovies()
        movies = moviesResult
    }

    fun getAllUpcomingMovies() {
        val moviesResult = getAllUpcomingMoviesUseCase.getAllUpcomingMovies()
        movies = moviesResult
    }

    fun getMoviesByGenre(genre: String) {
        val moviesResult = getMoviesByGenreUseCase.getMoviesByGenre(genre)
        movies = moviesResult
    }

    fun getTrendingDayMovies() {
        val moviesResult = getAllTrendingDayUseCase.getAllTrendingDayMovies()
        movies = moviesResult
    }

    fun getTrendingWeekMovies() {
        val moviesResult = getAllTrendingWeekUseCase.getAllTrendingWeekMovies()
        movies = moviesResult
    }

    fun getAllFavouriteMovies() {
        viewModelScope.launch {
            when (loadAccountUseCase.loadAccountDetails(getSessionIdUseCase.getSessionId())) {
                ResultParams.SUCCESS -> {
                    val account = getAccountDetailsUseCase.getAccountDetails()
                    val moviesResult = getAllFavouriteMoviesUseCase.getAllFavouriteMovies(
                        getSessionIdUseCase.getSessionId(),
                        account.id
                    )
                    movies = moviesResult
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun getAllRatedMovies() {
        viewModelScope.launch {
            val accountId = getAccountDetailsUseCase.getAccountDetails().id
            val moviesResult = getAllRatedMoviesUseCase.getAllRatedMovies(
                getSessionIdUseCase.getSessionId(),
                accountId
            )
            movies = moviesResult
        }
    }

//    fun getAllRatedMovies() {
//        viewModelScope.launch {
//            when (loadAccountUseCase.loadAccountDetails(getSessionIdUseCase.getSessionId())) {
//                ResultParams.SUCCESS -> {
//                    val account = getAccountDetailsUseCase.getAccountDetails()
//                    val moviesResult = getAllRatedMoviesUseCase.getAllRatedMovies(
//                        getSessionIdUseCase.getSessionId(),
//                        account.id
//                    )
//                    movies = moviesResult
//                }
//                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
//                ResultParams.NO_CONNECTION -> _error.value = true
//                ResultParams.NOT_RESPONSE -> _error.value = true
//            }
//        }
//    }

    fun getAllMoviesWatchlist() {
        viewModelScope.launch {
            when (loadAccountUseCase.loadAccountDetails(getSessionIdUseCase.getSessionId())) {
                ResultParams.SUCCESS -> {
                    val account = getAccountDetailsUseCase.getAccountDetails()
                    val moviesResult = getAllMoviesWatchlistUseCase.getAllMoviesWatchlist(
                        getSessionIdUseCase.getSessionId(),
                        account.id
                    )
                    movies = moviesResult
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }


}