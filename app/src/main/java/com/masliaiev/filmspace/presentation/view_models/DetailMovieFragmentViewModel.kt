package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.domain.entity.DetailedMovie
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.entity.Video
import com.masliaiev.filmspace.domain.usecases.*
import com.masliaiev.filmspace.helpers.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailMovieFragmentViewModel @Inject constructor(
    private val getAccountStateUseCase: GetAccountStateUseCase,
    private val getDetailedMovieUseCase: GetDetailedMovieUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val addRemoveToWatchlistUseCase: AddRemoveToWatchlistUseCase,
    private val markAsFavouriteUseCase: MarkAsFavouriteUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val rateMovieUseCase: RateMovieUseCase,
    private val deleteRatingUseCase: DeleteRatingUseCase,
    private val getVideosUseCase: GetVideosUseCase,
    private val getAppModeUseCase: GetAppModeUseCase
) : ViewModel() {

    private var _recommendations = MutableLiveData<List<Movie>>()
    val recommendations: LiveData<List<Movie>>
        get() = _recommendations

    private var _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>>
        get() = _similarMovies

    private var _accountMode = MutableLiveData<Boolean>()
    val accountMode: LiveData<Boolean>
        get() = _accountMode

    private var _detailedMovie = MutableLiveData<DetailedMovie>()
    val detailedMovie: LiveData<DetailedMovie>
        get() = _detailedMovie

    private var _favourite = MutableLiveData<DetailedButtonsState>()
    val favourite: LiveData<DetailedButtonsState>
        get() = _favourite

    private var _watchlist = MutableLiveData<DetailedButtonsState>()
    val watchlist: LiveData<DetailedButtonsState>
        get() = _watchlist

    private var _rateMovie = MutableLiveData<DetailedButtonsState>()
    val rateMovie: LiveData<DetailedButtonsState>
        get() = _rateMovie

    private var _video = MutableLiveData<Video>()
    val video: LiveData<Video>
        get() = _video

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError

    private var inWatchlist = false
    private var inFavourite = false
    private var isRated = false

    private var buttonWatchlistSavedState: DetailedButtonsState = Result(false)
    private var buttonFavouriteSavedState: DetailedButtonsState = Result(false)
    private var buttonRateSavedState: DetailedButtonsState = Result(false)

    private var wasLoaded = false
    var rateMarker = EMPTY_RATING

    fun loadData(movieId: Int) {
        if (!wasLoaded) {
            getDetailedMovie(movieId)
            getRecommendations(movieId)
            getSimilarMovies(movieId)
            getVideo(movieId)
            if (getAppModeUseCase.getAppMode() == AppConstants.SIGNED_IN_MODE) {
                _accountMode.value = true
                getAccountState(movieId)
            }
            wasLoaded = true
        }
    }


    private fun getAccountState(movieId: Int) {
        viewModelScope.launch {
            val result =
                getAccountStateUseCase.getAccountStates(movieId, getSessionIdUseCase.getSessionId())
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        parseWatchlistState(it.watchlist)
                        parseFavouriteState(it.favorite)
                        parseRatingState(it.rated)
                    }
                }
                ResultParams.ACCOUNT_ERROR -> setApiError()
                ResultParams.NO_CONNECTION -> setError()
                ResultParams.NOT_RESPONSE -> setError()
            }
        }
    }


    fun markAsFavourite(movieId: Int) {
        viewModelScope.launch {
            _favourite.value = Progress
            val result = markAsFavouriteUseCase.markAsFavourite(
                accountId = getAccountDetailsUseCase.getAccountDetails().id,
                sessionId = getSessionIdUseCase.getSessionId(),
                movieId = movieId,
                favourite = !inFavourite
            )
            when (result.first) {
                ResultParams.SUCCESS -> {
                    val accountState =
                        getAccountStateUseCase.getAccountStates(
                            movieId,
                            getSessionIdUseCase.getSessionId()
                        )
                    when (accountState.first) {
                        ResultParams.SUCCESS -> {
                            accountState.second?.favorite?.let { parseFavouriteState(it) }
                        }
                        ResultParams.ACCOUNT_ERROR -> _favourite.value = ApiError
                        ResultParams.NO_CONNECTION -> _favourite.value = Error
                        ResultParams.NOT_RESPONSE -> _favourite.value = Error
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _favourite.value = ApiError
                ResultParams.NO_CONNECTION -> _favourite.value = Error
                ResultParams.NOT_RESPONSE -> _favourite.value = Error
            }
        }
    }

    fun addRemoveToWatchlist(movieId: Int) {
        viewModelScope.launch {
            _watchlist.value = Progress
            val result = addRemoveToWatchlistUseCase.addToWatchlist(
                accountId = getAccountDetailsUseCase.getAccountDetails().id,
                sessionId = getSessionIdUseCase.getSessionId(),
                movieId = movieId,
                watchlist = !inWatchlist
            )
            when (result.first) {
                ResultParams.SUCCESS -> {
                    val accountState =
                        getAccountStateUseCase.getAccountStates(
                            movieId,
                            getSessionIdUseCase.getSessionId()
                        )
                    when (accountState.first) {
                        ResultParams.SUCCESS -> {
                            accountState.second?.watchlist?.let { parseWatchlistState(it) }
                        }
                        ResultParams.ACCOUNT_ERROR -> _watchlist.value = ApiError
                        ResultParams.NO_CONNECTION -> _watchlist.value = Error
                        ResultParams.NOT_RESPONSE -> _watchlist.value = Error
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _watchlist.value = ApiError
                ResultParams.NO_CONNECTION -> _watchlist.value = Error
                ResultParams.NOT_RESPONSE -> _watchlist.value = Error
            }
        }
    }

    fun rateMovie(rateValue: Double, movieId: Int) {
        viewModelScope.launch {
            _rateMovie.value = Progress
            val result = rateMovieUseCase.rateMovie(
                rateValue = rateValue,
                movieId = movieId,
                sessionId = getSessionIdUseCase.getSessionId()
            )
            when (result.first) {
                ResultParams.SUCCESS -> {
                    val accountState =
                        getAccountStateUseCase.getAccountStates(
                            movieId,
                            getSessionIdUseCase.getSessionId()
                        )
                    when (accountState.first) {
                        ResultParams.SUCCESS -> {
                            accountState.second?.rated?.let { parseRatingState(it) }
                        }
                        ResultParams.ACCOUNT_ERROR -> _rateMovie.value = ApiError
                        ResultParams.NO_CONNECTION -> _rateMovie.value = Error
                        ResultParams.NOT_RESPONSE -> _rateMovie.value = Error
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _rateMovie.value = ApiError
                ResultParams.NO_CONNECTION -> _rateMovie.value = Error
                ResultParams.NOT_RESPONSE -> _rateMovie.value = Error
            }
        }
    }

    fun deleteRating(movieId: Int) {
        viewModelScope.launch {
            _rateMovie.value = Progress
            val result = deleteRatingUseCase.deleteRating(
                movieId = movieId,
                sessionId = getSessionIdUseCase.getSessionId()
            )
            when (result.first) {
                ResultParams.SUCCESS -> {
                    val accountState =
                        getAccountStateUseCase.getAccountStates(
                            movieId,
                            getSessionIdUseCase.getSessionId()
                        )
                    when (accountState.first) {
                        ResultParams.SUCCESS -> {
                            accountState.second?.rated?.let { parseRatingState(it) }
                        }
                        ResultParams.ACCOUNT_ERROR -> _rateMovie.value = ApiError
                        ResultParams.NO_CONNECTION -> _rateMovie.value = Error
                        ResultParams.NOT_RESPONSE -> _rateMovie.value = Error
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _rateMovie.value = ApiError
                ResultParams.NO_CONNECTION -> _rateMovie.value = Error
                ResultParams.NOT_RESPONSE -> _rateMovie.value = Error
            }
        }
    }

    fun setSavedWatchlistState() {
        _watchlist.value = buttonWatchlistSavedState
    }

    fun setSavedFavouriteState() {
        _favourite.value = buttonFavouriteSavedState
    }

    fun setSavedRateState() {
        _rateMovie.value = buttonRateSavedState
    }

    private fun parseWatchlistState(state: Boolean) {
        val result = if (state) {
            Result(true)
        } else {
            Result(false)
        }
        inWatchlist = state
        _watchlist.value = result
        buttonWatchlistSavedState = result
    }

    private fun parseFavouriteState(state: Boolean) {
        val result = if (state) {
            Result(true)
        } else {
            Result(false)
        }
        inFavourite = state
        _favourite.value = result
        buttonFavouriteSavedState = result
    }

    private fun parseRatingState(state: Any) {
        if (state is Boolean) {
            _rateMovie.value = Result(false)
            buttonRateSavedState = Result(false)
            isRated = false
        } else {
            val userRating = state.toString()
            val result = if (userRating[8].toString() == ".") {
                Result(
                    true,
                    userRating.subSequence(7, 10).toString()
                )
            } else {
                Result(
                    true,
                    userRating.subSequence(7, 11).toString()
                )
            }
            _rateMovie.value = result
            buttonRateSavedState = result
            isRated = true
        }
    }

    private fun getVideo(movieId: Int) {
        viewModelScope.launch {
            val result = getVideosUseCase.getVideos(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _video.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> setApiError()
                ResultParams.NO_CONNECTION -> setError()
                ResultParams.NOT_RESPONSE -> setError()
            }
        }
    }

    private fun getDetailedMovie(movieId: Int) {
        viewModelScope.launch {
            val result = getDetailedMovieUseCase.getDetailedMovie(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _detailedMovie.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> setApiError()
                ResultParams.NO_CONNECTION -> setError()
                ResultParams.NOT_RESPONSE -> setError()
            }
        }
    }


    private fun getRecommendations(movieId: Int) {
        viewModelScope.launch {
            val result = getRecommendationsUseCase.getRecommendations(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _recommendations.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> setApiError()
                ResultParams.NO_CONNECTION -> setError()
                ResultParams.NOT_RESPONSE -> setError()
            }
        }
    }

    private fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            val result = getSimilarMoviesUseCase.getSimilarMovies(movieId)
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _similarMovies.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> setApiError()
                ResultParams.NO_CONNECTION -> setError()
                ResultParams.NOT_RESPONSE -> setError()
            }
        }
    }

    private fun setApiError() {
        if (_apiError.value != true) {
            _apiError.value = true
        }
    }

    private fun setError() {
        if (_error.value != true) {
            _error.value = true
        }
    }

    companion object {
        private const val EMPTY_RATING = 0.0
    }
}