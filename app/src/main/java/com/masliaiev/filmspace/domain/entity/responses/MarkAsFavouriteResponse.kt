package com.masliaiev.filmspace.domain.entity.responses

data class MarkAsFavouriteResponse(
    val success: Boolean,
    val statusCode: Int,
    val statusMassage: String
)
