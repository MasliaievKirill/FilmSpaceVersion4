package com.masliaiev.filmspace.data.network

import com.masliaiev.filmspace.data.network.models.account.AccountDto
import com.masliaiev.filmspace.data.network.models.genres.GenresListDto
import com.masliaiev.filmspace.data.network.models.movies.MoviesListDto
import com.masliaiev.filmspace.data.network.models.movies.account_states.AccountStatesDto
import com.masliaiev.filmspace.data.network.models.movies.details.DetailedMovieDto
import com.masliaiev.filmspace.data.network.models.movies.videos.VideoListDto
import com.masliaiev.filmspace.data.network.models.requests.*
import com.masliaiev.filmspace.data.network.models.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("authentication/token/new")
    suspend fun createRequestToken(
    ): Response<CreateRequestTokenResponseDto>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body createSessionRequestDto: CreateSessionRequestDto
    ): Response<CreateSessionResponseDto>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSessionRequest(
        @Body deleteSessionRequestDto: DeleteSessionRequestDto
    ): Response<DeleteSessionResponseDto>

    @GET("account")
    suspend fun getAccountDetails(
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String
    ): Response<AccountDto>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavouriteMovies(
        @Path(QUERY_ACCOUNT_ID) accountId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path(QUERY_ACCOUNT_ID) accountId: Int,
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getMoviesWatchlist(
        @Path(QUERY_ACCOUNT_ID) accountId: Int,
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @POST("account/{account_id}/favorite")
    suspend fun markAsFavourite(
        @Header(QUERY_HEADER_CONTENT_TYPE) headerContentType: String = HEADER_CONTENT_TYPE,
        @Path(QUERY_ACCOUNT_ID) accountId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Body markAsFavouriteRequestDto: MarkAsFavouriteRequestDto
    ): Response<MarkAsFavouriteResponseDto>

    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
        @Header(QUERY_HEADER_CONTENT_TYPE) headerContentType: String = HEADER_CONTENT_TYPE,
        @Path(QUERY_ACCOUNT_ID) accountId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Body addToWatchlistRequestDto: AddToWatchlistRequestDto
    ): Response<AddToWatchlistResponseDto>

    @GET("genre/movie/list")
    suspend fun getGenresList(
        @Query(QUERY_PARAMS_LANGUAGE) language: String
    ): Response<GenresListDto>

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path(QUERY_PARAM_MEDIA_TYPE) mediaType: String,
        @Path(QUERY_PARAM_TIME_WINDOW) timeWindow: String,
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE
    ): Response<MoviesListDto>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_SORT_BY) sorted: String = SORT_BY_POPULARITY,
        @Query(QUERY_PARAMS_MIN_VOTE_COUNT) minVoteCount: String = MIN_VOTE_COUNT,
        @Query(QUERY_PARAMS_WITH_GENRES) genre: String,
        @Query(QUERY_PARAMS_PAGE) page: Int

    ): Response<MoviesListDto>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_SEARCH) query: String,
        @Query(QUERY_PARAMS_PAGE) page: Int

    ): Response<MoviesListDto>

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAMS_LANGUAGE) language: String

    ): Response<DetailedMovieDto>

    @GET("movie/{movie_id}/account_states")
    suspend fun getAccountStates(
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String

    ): Response<AccountStatesDto>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAMS_LANGUAGE) language: String

    ): Response<MoviesListDto>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAMS_LANGUAGE) language: String,
        @Query(QUERY_PARAMS_PAGE) page: Int = DEFAULT_PAGE

    ): Response<MoviesListDto>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
    ): Response<VideoListDto>

    @POST("movie/{movie_id}/rating")
    suspend fun rateMovie(
        @Header(QUERY_HEADER_CONTENT_TYPE) headerContentType: String = HEADER_CONTENT_TYPE,
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String,
        @Body rateMovieRequestDto: RateMovieRequestDto
    ): Response<RateMovieResponseDto>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRatingMovie(
        @Header(QUERY_HEADER_CONTENT_TYPE) headerContentType: String = HEADER_CONTENT_TYPE,
        @Path(QUERY_PARAM_MOVIE_ID) movieId: Int,
        @Query(QUERY_PARAM_SESSION_ID) sessionId: String
    ): Response<DeleteRatingResponseDto>


    companion object {

        private const val QUERY_PARAM_SESSION_ID = "session_id"
        private const val QUERY_PARAMS_LANGUAGE = "language"
        private const val QUERY_PARAMS_PAGE = "page"
        private const val QUERY_ACCOUNT_ID = "account_id"
        private const val QUERY_HEADER_CONTENT_TYPE = "Content-Type"
        private const val QUERY_PARAM_MEDIA_TYPE = "media_type"
        private const val QUERY_PARAM_TIME_WINDOW = "time_window"
        private const val QUERY_PARAMS_SORT_BY = "sort_by"
        private const val QUERY_PARAMS_MIN_VOTE_COUNT = "vote_count.gte"
        private const val QUERY_PARAMS_WITH_GENRES = "with_genres"
        private const val QUERY_PARAMS_SEARCH = "query"
        private const val QUERY_PARAM_MOVIE_ID = "movie_id"

        private const val DEFAULT_PAGE = 1
        private const val HEADER_CONTENT_TYPE = "application/json"
        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val MIN_VOTE_COUNT = "1000"

    }
}