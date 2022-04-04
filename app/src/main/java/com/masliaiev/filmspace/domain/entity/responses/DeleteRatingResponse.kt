package com.masliaiev.filmspace.domain.entity.responses

data class DeleteRatingResponse(
    val success: Boolean,
    val statusCode: Int,
    val statusMassage: String
)
