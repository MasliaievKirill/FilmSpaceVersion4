package com.masliaiev.filmspace.data.network.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateRequestTokenResponseDto(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("expires_at")
    @Expose
    val expiresAt: String,
    @SerializedName("request_token")
    @Expose
    val requestToken: String
)
