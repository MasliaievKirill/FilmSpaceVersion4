package com.masliaiev.filmspace.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.data.database.AppDao
import com.masliaiev.filmspace.data.mapper.ModelsMapper
import com.masliaiev.filmspace.data.network.ApiService
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.requests.*
import com.masliaiev.filmspace.domain.entity.responses.*
import com.masliaiev.filmspace.domain.repository.AppRepository
import java.lang.Exception
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val apiService: ApiService,
    private val modelsMapper: ModelsMapper
) : AppRepository {

    override suspend fun createRequestToken(): CreateRequestTokenResponse {
        return modelsMapper.mapCreateRequestTokenResponseDtoToCreateRequestTokenResponseEntity(
            apiService.createRequestToken()
        )
    }

    override suspend fun createSession(
        createSessionRequest: CreateSessionRequest
    ): CreateSessionResponse {
        return try {
            modelsMapper.mapCreateSessionResponseDtoToCreateSessionResponseEntity(
                apiService.createSession(
                    createSessionRequestDto =
                    modelsMapper.mapCreateSessionRequestEntityToCreateSessionRequestDto(
                        createSessionRequest
                    )
                )
            )
        } catch (e: Exception){
            CreateSessionResponse(success = false, sessionId = "null")
        }

    }

    override suspend fun deleteSession(
        deleteSessionRequest: DeleteSessionRequest
    ): DeleteSessionResponse {
        return modelsMapper.mapDeleteSessionResponseDtoToDeleteSessionResponseEntity(
            apiService.deleteSessionRequest(
                modelsMapper.mapDeleteSessionRequestEntityToDeleteSessionRequestDto(
                    deleteSessionRequest
                )
            )
        )
    }

    override suspend fun getAccountDetails(sessionId: String): Account {
        TODO("Not yet implemented")
    }

    override fun getFavouriteMoviesList(sessionId: String, accountId: Int): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getRatedMoviesList(sessionId: String, accountId: Int): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getMoviesWatchlist(sessionId: String, accountId: Int): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun markAsFavourite(markAsFavouriteRequest: MarkAsFavouriteRequest): MarkAsFavouriteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addToWatchlist(addToWatchlistRequest: AddToWatchlistRequest): AddToWatchlistResponse {
        TODO("Not yet implemented")
    }

    override fun getGenresList(): LiveData<List<Genre>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailedMovie(movieId: Int): DetailedMovie {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountStates(movieId: Int, sessionId: String): AccountStates {
        TODO("Not yet implemented")
    }

    override fun getPopularMovies(): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getTopRatedMovies(): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getNowPlayingMovies(): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getUpcomingMovies(): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getRecommendations(movieId: Int): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getSimilarMovies(movieId: Int): LiveData<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideos(movieId: Int): List<Video> {
        TODO("Not yet implemented")
    }

    override suspend fun rateMovie(
        movieId: Int,
        rateMovieRequest: RateMovieRequest
    ): RateMovieResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRating(movieId: Int, sessionId: String): DeleteRatingResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovies(lang: String, query: String): LiveData<PagingData<Movie>> {
        TODO("Not yet implemented")
    }
}