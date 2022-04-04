package com.masliaiev.filmspace.domain.entity.responses

data class AddToWatchlistResponse(
    val success: Boolean,
    val statusCode: Int,
    val statusMassage: String
)
