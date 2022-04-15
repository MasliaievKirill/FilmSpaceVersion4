package com.masliaiev.filmspace.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.requests.DeleteSessionRequest
import com.masliaiev.filmspace.domain.entity.requests.RateMovieRequest
import com.masliaiev.filmspace.domain.entity.responses.*
import com.masliaiev.filmspace.helpers.ResultParams

interface AppRepository {

    //AUTHENTICATION

    suspend fun createRequestToken(): Pair<ResultParams, CreateRequestTokenResponse?>

    suspend fun createSession(requestToken: String): CreateSessionResponse //POST

    suspend fun deleteSession(deleteSessionRequest: DeleteSessionRequest): DeleteSessionResponse //DELETE


    //ACCOUNT

    fun getAppMode(): String

    fun setAppMode(appMode: String)

    fun getSessionId(): String

    fun setSessionId(sessionId: String)

    suspend fun loadAccountDetails(sessionId: String): ResultParams

    suspend fun getAccountDetails(): Account

    suspend fun getFavouriteMovies(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?>

    suspend fun getRatedMovies(sessionId: String, accountId: Int): Pair<ResultParams, List<Movie>?>

    suspend fun getMoviesWatchlist(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?>

    suspend fun markAsFavourite(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        favourite: Boolean
    ): Pair<ResultParams, MarkAsFavouriteResponse?>

    suspend fun addToWatchlist(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        watchlist: Boolean
    ): Pair<ResultParams,AddToWatchlistResponse?>

    //GENRES

    suspend fun getGenresList(): Pair<ResultParams,List<Genre>?>

    //MOVIES

    suspend fun getDetailedMovie(movieId: Int): DetailedMovie

    suspend fun getAccountStates(movieId: Int, sessionId: String): AccountStates

    suspend fun getPopularMovies(): Pair<ResultParams, List<Movie>?>

    suspend fun getTopRatedMovies(): Pair<ResultParams, List<Movie>?>

    suspend fun getNowPlayingMovies(): Pair<ResultParams, List<Movie>?>

    suspend fun getUpcomingMovies(): Pair<ResultParams, List<Movie>?>

    suspend fun getTrendingDayMovies(): Pair<ResultParams, List<Movie>?>

    suspend fun getTrendingWeekMovies(): Pair<ResultParams, List<Movie>?>

    fun getRecommendations(movieId: Int): LiveData<List<Movie>>

    fun getSimilarMovies(movieId: Int): LiveData<List<Movie>>

    suspend fun getVideos(movieId: Int): List<Video>

    suspend fun rateMovie(
        movieId: Int,
        rateMovieRequest: RateMovieRequest
    ): RateMovieResponse //header "application/json;charset=utf-8"

    suspend fun deleteRating(movieId: Int, sessionId: String): DeleteRatingResponse

    //SEARCH

    suspend fun searchMovies(lang: String, query: String): LiveData<PagingData<Movie>>


}