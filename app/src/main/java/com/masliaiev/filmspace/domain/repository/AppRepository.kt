package com.masliaiev.filmspace.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.requests.*
import com.masliaiev.filmspace.domain.entity.responses.*

interface AppRepository {

    //AUTHENTICATION

    suspend fun createRequestToken(): CreateRequestTokenResponse

    suspend fun createSession(createSessionRequest: CreateSessionRequest): CreateSessionRequest //POST

    suspend fun deleteSession(deleteSessionRequest: DeleteSessionRequest): DeleteSessionRequest //DELETE


    //ACCOUNT

    suspend fun getAccountDetails(sessionId: String): Account

    fun getFavouriteMoviesList(sessionId: String, accountId: Int): LiveData<List<Movie>>

    fun getRatedMoviesList(sessionId: String, accountId: Int): LiveData<List<Movie>>

    fun getMoviesWatchlist(sessionId: String, accountId: Int): LiveData<List<Movie>>

    suspend fun markAsFavourite(markAsFavouriteRequest: MarkAsFavouriteRequest): MarkAsFavouriteResponse //header "application/json;charset=utf-8"

    suspend fun addToWatchlist(addToWatchlistRequest: AddToWatchlistRequest): AddToWatchlistResponse  //header "application/json;charset=utf-8"

    //GENRES

    fun getGenresList(): LiveData<List<Genre>>

    //MOVIES

    suspend fun getDetailedMovie(movieId: Int): DetailedMovie

    suspend fun getAccountStates(movieId: Int, sessionId: String): AccountStates

    fun getPopularMovies(): LiveData<List<Movie>>

    fun getTopRatedMovies(): LiveData<List<Movie>>

    fun getNowPlayingMovies(): LiveData<List<Movie>>

    fun getUpcomingMovies(): LiveData<List<Movie>>

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