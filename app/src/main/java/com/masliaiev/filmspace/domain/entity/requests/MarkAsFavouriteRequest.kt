package com.masliaiev.filmspace.domain.entity.requests

data class MarkAsFavouriteRequest(
    val mediaType: String,
    val mediaId: Int,
    val favorite: Boolean
)
