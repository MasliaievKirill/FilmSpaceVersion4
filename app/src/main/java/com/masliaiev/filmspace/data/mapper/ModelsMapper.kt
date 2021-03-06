package com.masliaiev.filmspace.data.mapper

import android.graphics.Color
import com.masliaiev.filmspace.data.database.models.AccountDbModel
import com.masliaiev.filmspace.data.network.models.account.AccountDto
import com.masliaiev.filmspace.data.network.models.genres.GenreDto
import com.masliaiev.filmspace.data.network.models.movies.MovieDto
import com.masliaiev.filmspace.data.network.models.movies.account_states.AccountStatesDto
import com.masliaiev.filmspace.data.network.models.movies.details.DetailedMovieDto
import com.masliaiev.filmspace.data.network.models.movies.videos.VideoDto
import com.masliaiev.filmspace.data.network.models.responses.CreateRequestTokenResponseDto
import com.masliaiev.filmspace.data.network.models.responses.CreateSessionResponseDto
import com.masliaiev.filmspace.domain.entity.*
import com.masliaiev.filmspace.domain.entity.responses.CreateRequestTokenResponse
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import javax.inject.Inject
import kotlin.random.Random

class ModelsMapper @Inject constructor() {

    fun mapCreateRequestTokenResponseDtoToCreateRequestTokenResponseEntity(
        createRequestTokenResponseDto: CreateRequestTokenResponseDto
    ): CreateRequestTokenResponse {
        return CreateRequestTokenResponse(
            success = createRequestTokenResponseDto.success,
            expiresAt = createRequestTokenResponseDto.expiresAt,
            requestToken = createRequestTokenResponseDto.requestToken
        )
    }


    fun mapCreateSessionResponseDtoToCreateSessionResponseEntity(
        createSessionResponseDto: CreateSessionResponseDto
    ): CreateSessionResponse {
        return CreateSessionResponse(
            success = createSessionResponseDto.success,
            sessionId = createSessionResponseDto.sessionId
        )
    }

    fun mapAccountDtoToAccountDbModel(accountDto: AccountDto): AccountDbModel {
        return AccountDbModel(
            avatarPath = GRAVATAR_URL + accountDto.avatar.gravatar.hash,
            id = accountDto.id,
            iso6391 = accountDto.iso6391,
            iso31661 = accountDto.iso31661,
            name = accountDto.name,
            includeAdult = accountDto.includeAdult,
            username = accountDto.username
        )
    }

    fun mapAccountDbModelToAccountEntity(accountDbModel: AccountDbModel): Account {
        return Account(
            avatarPath = accountDbModel.avatarPath,
            id = accountDbModel.id,
            iso6391 = accountDbModel.iso6391,
            iso31661 = accountDbModel.iso31661,
            name = accountDbModel.name,
            includeAdult = accountDbModel.includeAdult,
            username = accountDbModel.username
        )
    }

    fun mapMovieDtoToMovieEntity(movieDto: MovieDto): Movie {
        return Movie(
            adult = movieDto.adult,
            backdropPath = BASE_IMAGE_URL + IMAGE_SIZE_W500 + movieDto.backdropPath,
            genreIds = movieDto.genreIds,
            id = movieDto.id,
            originalLanguage = movieDto.originalLanguage,
            originalTitle = movieDto.originalTitle,
            overview = movieDto.overview,
            popularity = movieDto.popularity,
            posterPath = BASE_IMAGE_URL + IMAGE_SIZE_W500 + movieDto.posterPath,
            releaseDate = movieDto.releaseDate,
            title = movieDto.title,
            video = movieDto.video,
            voteAverage = String.format("%.1f", movieDto.voteAverage),
            voteCount = movieDto.voteCount
        )
    }

    fun mapGenreDtoToGenreEntity(genreDto: GenreDto): Genre {
        return Genre(
            id = genreDto.id,
            name = genreDto.name,
            uniqueColor = getColour()
        )
    }

    fun mapAccountStatesDtoToAccountStatesEntity(accountStatesDto: AccountStatesDto): AccountStates {
        return AccountStates(
            id = accountStatesDto.id,
            favorite = accountStatesDto.favorite,
            rated = accountStatesDto.rated,
            watchlist = accountStatesDto.watchlist
        )
    }

    fun mapDetailedMovieDtoToDetailedMovieEntity(detailedMovieDto: DetailedMovieDto): DetailedMovie {
        return DetailedMovie(
            adult = detailedMovieDto.adult,
            backdropPath = BASE_IMAGE_URL + IMAGE_SIZE_W780 + detailedMovieDto.backdropPath,
            budget = detailedMovieDto.budget,
            genres = parseGenresList(detailedMovieDto.genres),
            homepage = detailedMovieDto.homepage,
            id = detailedMovieDto.id,
            imdbId = detailedMovieDto.imdbId,
            originalLanguage = detailedMovieDto.originalLanguage,
            originalTitle = detailedMovieDto.originalTitle,
            overview = detailedMovieDto.overview,
            popularity = detailedMovieDto.popularity,
            posterPath = BASE_IMAGE_URL + IMAGE_SIZE_W500 + detailedMovieDto.posterPath,
            releaseDate = detailedMovieDto.releaseDate,
            revenue = detailedMovieDto.revenue,
            runtime = detailedMovieDto.runtime.toString(),
            status = detailedMovieDto.status,
            tagline = detailedMovieDto.tagline,
            title = detailedMovieDto.title,
            video = detailedMovieDto.video,
            voteCount = detailedMovieDto.voteCount,
            voteAverage = String.format("%.1f", detailedMovieDto.voteAverage)
        )

    }

    fun mapVideoDtoToVideoEntity(videoDto: VideoDto): Video {
        return Video(
            iso6391 = videoDto.iso6391,
            iso31661 = videoDto.iso31661,
            name = videoDto.name,
            key = BASE_YOUTUBE_URL + videoDto.key,
            site = videoDto.site,
            size = videoDto.size,
            type = videoDto.type,
            official = videoDto.official,
            publishedAt = videoDto.publishedAt,
            id = videoDto.id
        )
    }

    private fun getColour(): Int {
        val colors = listOf(
            Color.YELLOW,
            Color.BLUE,
            Color.RED,
            Color.MAGENTA,
            Color.CYAN,
            Color.GREEN
        )
        val position = Random.nextInt(colors.size)
        return colors[position]
    }

    private fun parseGenresList(genres: List<GenreDto>?): String {
        var genresList = ""
        genres?.let {
            for (position in genres.indices) {
                if (position == genres.size - 1) {
                    genresList += genres[position].name
                } else {
                    genresList += genres[position].name + ", "
                }
            }
        }
        return genresList
    }


    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
        private const val GRAVATAR_URL = "https://secure.gravatar.com/avatar/"
        private const val BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="

        private const val IMAGE_SIZE_W185 = "w185"
        private const val IMAGE_SIZE_W342 = "w342"
        private const val IMAGE_SIZE_W500 = "w500"
        private const val IMAGE_SIZE_W780 = "w780"
    }


}