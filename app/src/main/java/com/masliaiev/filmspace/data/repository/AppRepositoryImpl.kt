package com.masliaiev.filmspace.data.repository

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.data.database.AppDao
import com.masliaiev.filmspace.data.mapper.ModelsMapper
import com.masliaiev.filmspace.data.network.ApiService
import com.masliaiev.filmspace.data.network.models.requests.*
import com.masliaiev.filmspace.data.network.page_sources.*
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.responses.CreateRequestTokenResponse
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.repository.AppRepository
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.delay
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
    ): Pair<ResultParams, CreateSessionResponse?> {
        return try {
            val response = apiService.createSession(
                createSessionRequestDto = CreateSessionRequestDto(requestToken)
            )
            val session = response.body()

            when (response.code()) {
                200 -> {
                    Pair(
                        ResultParams.SUCCESS,
                        session?.let {
                            mapper.mapCreateSessionResponseDtoToCreateSessionResponseEntity(
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

    override suspend fun deleteSession(
        sessionId: String
    ): Pair<ResultParams, Boolean?> {
        return try {
            val response = apiService.deleteSessionRequest(
                deleteSessionRequestDto = DeleteSessionRequestDto(sessionId)
            )
            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, true)
                401 -> Pair(ResultParams.ACCOUNT_ERROR, false)
                else -> Pair(ResultParams.NOT_RESPONSE, false)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, false)
        }
    }

    override fun getAppMode(): String {
        return sharedPreferences.getString(AppConstants.KEY_APP_MODE, AppConstants.UNKNOWN_MODE)
            ?: AppConstants.UNKNOWN_MODE
    }

    override fun setAppMode(appMode: String) {
        sharedPreferences.edit().putString(AppConstants.KEY_APP_MODE, appMode).apply()
    }

    override fun getSessionId(): String {
        return sharedPreferences.getString(
            AppConstants.KEY_SESSION_ID,
            AppConstants.EMPTY_SESSION_ID
        ) ?: AppConstants.EMPTY_SESSION_ID
    }

    override fun setSessionId(sessionId: String) {
        sharedPreferences.edit().putString(AppConstants.KEY_SESSION_ID, sessionId).apply()
    }

    override fun checkShowGoogleReview(): Boolean {
        val previousShowTime = sharedPreferences.getLong(
            AppConstants.KEY_PREVIOUS_SHOW_TIME,
            AppConstants.EMPTY_PREVIOUS_SHOW_TIME
        )
        // 32 days = 2764800000L millis
        return when {
            previousShowTime == 0L || (System.currentTimeMillis() - previousShowTime) > 30000L -> {
                sharedPreferences.edit()
                    .putLong(AppConstants.KEY_PREVIOUS_SHOW_TIME, System.currentTimeMillis())
                    .apply()
                true
            }
            else -> false
        }
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
    ): Pair<ResultParams, Boolean?> {
        return try {
            val response = apiService.markAsFavourite(
                accountId = accountId,
                sessionId = sessionId,
                markAsFavouriteRequestDto = MarkAsFavouriteRequestDto(
                    MEDIA_TYPE_MOVIE,
                    movieId,
                    favourite
                )
            )
            when (response.code()) {
                200, 201 -> Pair(
                    ResultParams.SUCCESS, true
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
    ): Pair<ResultParams, Boolean?> {
        return try {
            val response = apiService.addToWatchlist(
                accountId = accountId,
                sessionId = sessionId,
                addToWatchlistRequestDto = AddToWatchlistRequestDto(
                    MEDIA_TYPE_MOVIE,
                    movieId,
                    watchlist
                )
            )
            when (response.code()) {
                200, 201 -> Pair(
                    ResultParams.SUCCESS, true
                )
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getGenresList(): Pair<ResultParams, List<Genre>?> {
        return try {
            val response = apiService.getGenresList(language = getCurrentLanguage())
            val genres = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, genres?.genresList?.map {
                    mapper.mapGenreDtoToGenreEntity(it)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getDetailedMovie(movieId: Int): Pair<ResultParams, DetailedMovie?> {
        return try {
            val response = apiService.getDetails(movieId = movieId, language = getCurrentLanguage())
            val detailedMovie = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, detailedMovie?.let {
                    mapper.mapDetailedMovieDtoToDetailedMovieEntity(detailedMovie)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun getAccountStates(
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, AccountStates?> {
        return try {
            val response = apiService.getAccountStates(movieId = movieId, sessionId = sessionId)
            val accountState = response.body()

            when (response.code()) {
                200 -> Pair(ResultParams.SUCCESS, accountState?.let {
                    mapper.mapAccountStatesDtoToAccountStatesEntity(accountState)
                })
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
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

    override suspend fun getTrendingDayMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getTrending(
                mediaType = MEDIA_TYPE_MOVIE,
                timeWindow = TIME_WINDOW_DAY,
                language = getCurrentLanguage()
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

    override suspend fun getTrendingWeekMovies(): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getTrending(
                mediaType = MEDIA_TYPE_MOVIE,
                timeWindow = TIME_WINDOW_WEEK,
                language = getCurrentLanguage()
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

    override fun getAllFavouriteMovies(
        sessionId: String,
        accountId: Int
    ) = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllFavouriteMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage(),
                sessionId,
                accountId
            )
        }
    ).liveData

    override fun getAllRatedMovies(sessionId: String, accountId: Int) = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllRatedMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage(),
                sessionId,
                accountId
            )
        }
    ).liveData

    override fun getAllMoviesWatchlist(
        sessionId: String,
        accountId: Int
    ) = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllMoviesWatchlistPageSource(
                apiService,
                mapper,
                getCurrentLanguage(),
                sessionId,
                accountId
            )
        }
    ).liveData

    override fun getAllPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllPopularMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage()
            )
        }
    ).liveData

    override fun getAllTopRatedMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllTopRatedMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage()
            )
        }
    ).liveData

    override fun getAllNowPlayingMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllNowPlayingMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage()
            )
        }
    ).liveData

    override fun getAllUpcomingMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllUpcomingMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage()
            )
        }
    ).liveData

    override fun getMoviesByGenre(genre: String) = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            MoviesByGenrePageSource(
                apiService,
                mapper,
                getCurrentLanguage(),
                genre
            )
        }
    ).liveData

    override fun getAllTrendingDayMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllTrendingMoviesPageSource(
                apiService,
                mapper,
                MEDIA_TYPE_MOVIE,
                TIME_WINDOW_DAY,
                getCurrentLanguage()
            )
        }
    ).liveData

    override fun getAllTrendingWeekMovies() = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            AllTrendingMoviesPageSource(
                apiService,
                mapper,
                MEDIA_TYPE_MOVIE,
                TIME_WINDOW_WEEK,
                getCurrentLanguage()
            )
        }
    ).liveData

    override suspend fun getRecommendations(movieId: Int): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getRecommendations(
                movieId = movieId,
                language = getCurrentLanguage()
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

    override suspend fun getSimilarMovies(movieId: Int): Pair<ResultParams, List<Movie>?> {
        return try {
            val response = apiService.getSimilarMovies(
                movieId = movieId,
                language = getCurrentLanguage()
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

    override suspend fun getVideos(movieId: Int): Pair<ResultParams, Video?> {
        return try {
            val response = apiService.getVideos(
                movieId
            )
            val videosList = response.body()

            when (response.code()) {
                200 -> {
                    videosList?.results?.let {
                        for (video in it) {
                            if (video.official) {
                                return Pair(
                                    ResultParams.SUCCESS,
                                    mapper.mapVideoDtoToVideoEntity(video)
                                )
                            }
                        }
                    }
                    Pair(ResultParams.SUCCESS, null)
                }
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun rateMovie(
        rateValue: Double,
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, Boolean?> {
        return try {
            val response = apiService.rateMovie(
                movieId = movieId,
                sessionId = sessionId,
                rateMovieRequestDto = RateMovieRequestDto(
                    rateValue
                )
            )
            delay(2000)
            when (response.code()) {
                200, 201 -> Pair(
                    ResultParams.SUCCESS, true
                )
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override suspend fun deleteRating(
        movieId: Int,
        sessionId: String
    ): Pair<ResultParams, Boolean?> {
        return try {
            val response = apiService.deleteRatingMovie(
                movieId = movieId,
                sessionId = sessionId
            )
            delay(2000)
            when (response.code()) {
                200, 201 -> Pair(
                    ResultParams.SUCCESS, true
                )
                401 -> Pair(ResultParams.ACCOUNT_ERROR, null)
                else -> Pair(ResultParams.NOT_RESPONSE, null)
            }
        } catch (e: Exception) {
            Pair(ResultParams.NO_CONNECTION, null)
        }
    }

    override fun searchMovies(query: String) = Pager(
        config = PagingConfig(
            pageSize = MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE,
            maxSize = MAX_NUMBER_OF_ITEMS,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            SearchMoviesPageSource(
                apiService,
                mapper,
                getCurrentLanguage(),
                query
            )
        }
    ).liveData

    private fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }

    companion object {
        private const val MEDIA_TYPE_MOVIE = "movie"
        private const val MEDIA_TYPE_PERSON = "person"
        private const val TIME_WINDOW_DAY = "day"
        private const val TIME_WINDOW_WEEK = "week"

        private const val MAX_NUMBER_OF_ITEMS_LOADED_AT_ONCE = 20
        private const val MAX_NUMBER_OF_ITEMS = 300
    }
}