package com.masliaiev.filmspace.domain.entity.responses

data class RateMovieResponse(
    val success: Boolean,
    val statusCode: Int,
    val statusMassage: String
)
