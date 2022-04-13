package com.masliaiev.filmspace.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.data.database.AppDao
import com.masliaiev.filmspace.data.mapper.ModelsMapper
import com.masliaiev.filmspace.data.network.ApiService
import com.masliaiev.filmspace.data.network.models.requests.AddToWatchlistRequestDto
import com.masliaiev.filmspace.data.network.models.requests.CreateSessionRequestDto
import com.masliaiev.filmspace.data.network.models.requests.MarkAsFavouriteRequestDto
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.requests.DeleteSessionRequest
import com.masliaiev.filmspace.domain.entity.requests.RateMovieRequest
import com.masliaiev.filmspace.domain.entity.responses.*
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import java.util.*
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val apiService: ApiService,
    private val mapper: ModelsMapper,
    private val sharedPreferences: SharedPreferences
) : AppRepository {

    override suspend fun createRequestToken(): Pair<ResultParams, CreateRequestTokenResponse?> {
        return try {
            val response = apiService.createRequestToken()
            val requestToken = response.body()

            when (response.code()) {
                200 -> {
                    Pair(
                        ResultParams.SUCCESS,
                        requestToken?.let {
                            mapper.mapCreateRequestTokenResponseDtoToCreateRequestTokenResponseEntity(
                                it
                            )
                        })
                }
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }

        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun createSession(
        requestToken: String
    ): CreateSessionResponse {
        return try {
            mapper.mapCreateSessionResponseDtoToCreateSessionResponseEntity(
                apiService.createSession(
                    createSessionRequestDto = CreateSessionRequestDto(requestToken)
                )
            )
        } catch (e: Exception) {
            CreateSessionResponse(success = false, sessionId = "null")
        }

    }

    override suspend fun deleteSession(
        deleteSessionRequest: DeleteSessionRequest
    ): DeleteSessionResponse {
        return mapper.mapDeleteSessionResponseDtoToDeleteSessionResponseEntity(
            apiService.deleteSessionRequest(
                deleteSessionRequestDto =
                mapper.mapDeleteSessionRequestEntityToDeleteSessionRequestDto(
                    deleteSessionRequest
                )
            )
        )
    }

    override fun getAppMode(): String {
        return sharedPreferences.getString(AppConstants.KEY_APP_MODE, AppConstants.UNKNOWN_MODE) ?: AppConstants.UNKNOWN_MODE
    }

    override fun setAppMode(appMode: String) {
        sharedPreferences.edit().putString(AppConstants.KEY_APP_MODE, appMode).apply()
    }

    override fun getSessionId(): String {
        return sharedPreferences.getString(AppConstants.KEY_SESSION_ID, AppConstants.EMPTY_SESSION_ID) ?: AppConstants.EMPTY_SESSION_ID
    }

    override fun setSessionId(sessionId: String) {
        sharedPreferences.edit().putString(AppConstants.KEY_SESSION_ID, sessionId).apply()
    }

    override suspend fun loadAccountDetails(sessionId: String): ResultParams {
        return try {
            val accountResponse = apiService.getAccountDetails(sessionId = sessionId)
            val account = accountResponse.body()

            when (accountResponse.code()) {
                200 -> {
                    account?.let {
                        appDao.addAccount(mapper.mapAccountDtoToAccountDbModel(it))
                    }
                    ResultParams.SUCCESS
                }
                401 -> ResultParams.ACCOUNT_ERROR
                else -> ResultParams.NOT_RESPONSE
            }

        } catch (e: Exception) {
            ResultParams.NO_CONNECTION
        }

    }

    override suspend fun getAccountDetails(): Account {
        return mapper.mapAccountDbModelToAccountEntity(appDao.getAccount())
    }

    override suspend fun getFavouriteMovies(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getFavouriteMovies(
                accountId = accountId, sessionId = sessionId, language = getCurrentLanguage()
            )
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getRatedMovies(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getRatedMovies(
                accountId = accountId, sessionId = sessionId, language = getCurrentLanguage()
            )
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getMoviesWatchlist(
        sessionId: String,
        accountId: Int
    ): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getMoviesWatchlist(
                accountId = accountId, sessionId = sessionId, language = getCurrentLanguage()
            )
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun markAsFavourite(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        favourite: Boolean
    ): Pair<ResultParams, MarkAsFavouriteResponse?> {
        return try {
            val response = apiService.markAsFavourite(
                accountId = accountId,
                sessionId = sessionId,
                markAsFavouriteRequestDto = MarkAsFavouriteRequestDto(
                    MEDIA_TYPE,
                    movieId,
                    favourite
                )
            )
            val markAsFavouriteResponse = response.body()

            when (response.code()) {
                200 -> Pair(
                    ResultParams.SUCCESS, markAsFavouriteResponse?.let {
                        mapper.markAsFavouriteResponseDtoToMarkAsFavouriteResponseEntity(
                            it
                        )
                    }
                )
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun addToWatchlist(
        accountId: Int,
        sessionId: String,
        movieId: Int,
        watchlist: Boolean
    ): Pair<ResultParams, AddToWatchlistResponse?> {
        return try {
            val response = apiService.addToWatchlist(
                accountId = accountId,
                sessionId = sessionId,
                addToWatchlistRequestDto = AddToWatchlistRequestDto(
                    MEDIA_TYPE,
                    movieId,
                    watchlist
                )
            )
            val addToWatchlistResponse = response.body()

            when (response.code()) {
                200 -> Pair(
                    ResultParams.SUCCESS, addToWatchlistResponse?.let {
                        mapper.addToWatchlistResponseDtoToAddToWatchlistResponseEntity(
                            it
                        )
                    }
                )
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
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

    override suspend fun getPopularMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getPopularMovies(language = getCurrentLanguage())
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getTopRatedMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getTopRatedMovies(language = getCurrentLanguage())
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getNowPlayingMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getNowPlayingMovies(language = getCurrentLanguage())
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }


    }

    override suspend fun getUpcomingMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getUpcomingMovies(language = getCurrentLanguage())
            val moviesList = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, moviesList?.results?.map {
                    mapper.mapMovieDtoToMovieEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
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

    private fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }

    companion object {
        private const val MEDIA_TYPE = "movie"
    }
}