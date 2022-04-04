package com.masliaiev.filmspace.domain.entity.requests

data class AddToWatchlistRequest(
    val mediaType: String,
    val mediaId: Int,
    val favorite: Boolean
)
