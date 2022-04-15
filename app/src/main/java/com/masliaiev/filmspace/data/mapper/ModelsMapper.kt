package com.masliaiev.filmspace.data.mapper

import android.graphics.Color
import com.masliaiev.filmspace.data.database.models.AccountDbModel
import com.masliaiev.filmspace.data.network.models.account.AccountDto
import com.masliaiev.filmspace.data.network.models.genres.GenreDto
import com.masliaiev.filmspace.data.network.models.movies.MovieDto
import com.masliaiev.filmspace.data.network.models.requests.DeleteSessionRequestDto
import com.masliaiev.filmspace.data.network.models.responses.*
import com.masliaiev.filmspace.domain.entity.Account
import com.masliaiev.filmspace.domain.entity.Genre
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.entity.requests.DeleteSessionRequest
import com.masliaiev.filmspace.domain.entity.responses.*
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

    fun mapDeleteSessionRequestEntityToDeleteSessionRequestDto(
        deleteSessionRequest: DeleteSessionRequest
    ): DeleteSessionRequestDto {
        return DeleteSessionRequestDto(
            sessionId = deleteSessionRequest.sessionId
        )
    }

    fun mapDeleteSessionResponseDtoToDeleteSessionResponseEntity(
        deleteSessionResponseDto: DeleteSessionResponseDto
    ): DeleteSessionResponse {
        return DeleteSessionResponse(
            success = deleteSessionResponseDto.success
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
            title = movieDto.originalTitle,
            video = movieDto.video,
            voteAverage = movieDto.voteAverage,
            voteCount = movieDto.voteCount
        )
    }

    fun markAsFavouriteResponseDtoToMarkAsFavouriteResponseEntity(
        markAsFavouriteResponseDto: MarkAsFavouriteResponseDto
    ): MarkAsFavouriteResponse {
        return MarkAsFavouriteResponse(
            success = markAsFavouriteResponseDto.success,
            statusCode = markAsFavouriteResponseDto.statusCode,
            statusMassage = markAsFavouriteResponseDto.statusMassage
        )
    }

    fun addToWatchlistResponseDtoToAddToWatchlistResponseEntity(
        addToWatchlistResponseDto: AddToWatchlistResponseDto
    ): AddToWatchlistResponse {
        return AddToWatchlistResponse(
            success = addToWatchlistResponseDto.success,
            statusCode = addToWatchlistResponseDto.statusCode,
            statusMassage = addToWatchlistResponseDto.statusMassage
        )
    }

    fun mapGenreDtoToGenreEntity(genreDto: GenreDto): Genre {
        return Genre(
            id = genreDto.id,
            name = genreDto.name,
            uniqueColor = getColour()
        )
    }

    private fun getColour(): Int {
        val colors = listOf(
            Color.YELLOW,
            Color.BLUE,
            Color.GRAY,
            Color.RED,
            Color.MAGENTA,
            Color.DKGRAY,
            Color.LTGRAY
        )
        val position = Random.nextInt(colors.size)
        return colors[position]
    }


    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
        private const val GRAVATAR_URL = "https://secure.gravatar.com/avatar/"

        private const val IMAGE_SIZE_W185 = "w185"
        private const val IMAGE_SIZE_W342 = "w342"
        private const val IMAGE_SIZE_W500 = "w500"
    }


}