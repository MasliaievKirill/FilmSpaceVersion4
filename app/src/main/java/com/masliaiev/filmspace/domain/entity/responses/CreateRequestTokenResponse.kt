package com.masliaiev.filmspace.domain.entity.responses

data class CreateRequestTokenResponse(
    val success: Boolean,
    val expiresAt: String,
    val requestToken: String
)
