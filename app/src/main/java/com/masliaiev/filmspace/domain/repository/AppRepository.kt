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

    suspend fun createSession(requestToken: String): Pair<ResultParams, CreateSessionResponse?>  //POST

    suspend fun deleteSession(sessionId: String): Pair<ResultParams, Boolean?> //DELETE


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

    fun getAllFavouriteMovies(
        sessionId: String,
        accountId: Int
    ): LiveData<PagingData<Movie>>

    suspend fun getRatedMovies(sessionId: String, accountId: Int): Pair<ResultParams, List<Movie>?>

    fun getAllRatedMovies(
        sessionId: String,
        accountId: Int
    ): LiveData<PagingData<Movie>>

    suspend fun getMoviesWatchlist(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?>

    fun getAllMoviesWatchlist(
        sessionId: String,
        accountId: Int
    ): LiveData<PagingData<Movie>>

    suspend fun markAsFavourite(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        favourite: Boolean
    ): Pair<ResultParams, Boolean?>

    suspend fun addToWatchlist(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        watchlist: Boolean
    ): Pair<ResultParams, Boolean?>

    //GENRES

    suspend fun getGenresList(): Pair<ResultParams, List<Genre>?>

    //MOVIES

    suspend fun getDetailedMovie(movieId: Int): Pair<ResultParams, DetailedMovie?>

    suspend fun getAccountStates(movieId: Int, sessionId: String): Pair<ResultParams, AccountStates?>

    suspend fun getPopularMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllPopularMovies(): LiveData<PagingData<Movie>>

    suspend fun getTopRatedMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllTopRatedMovies(): LiveData<PagingData<Movie>>

    suspend fun getNowPlayingMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllNowPlayingMovies(): LiveData<PagingData<Movie>>

    suspend fun getUpcomingMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllUpcomingMovies(): LiveData<PagingData<Movie>>

    suspend fun getTrendingDayMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllTrendingDayMovies(): LiveData<PagingData<Movie>>

    suspend fun getTrendingWeekMovies(): Pair<ResultParams, List<Movie>?>

    fun getAllTrendingWeekMovies(): LiveData<PagingData<Movie>>

    fun getMoviesByGenre(genre: String): LiveData<PagingData<Movie>>

    suspend fun getRecommendations(movieId: Int): Pair<ResultParams, List<Movie>?>

    suspend fun getSimilarMovies(movieId: Int): Pair<ResultParams, List<Movie>?>

    suspend fun getVideos(movieId: Int): Pair<ResultParams, List<Video>?>

    suspend fun rateMovie(
        rateValue: Double,
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, Boolean?>

    suspend fun deleteRating(movieId: Int, sessionId: String): Pair<ResultParams, Boolean?>

    //SEARCH

    fun searchMovies(query: String): LiveData<PagingData<Movie>>


}