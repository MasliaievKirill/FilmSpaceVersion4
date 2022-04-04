package com.masliaiev.filmspace.data.network.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateSessionRequestDto(
    @SerializedName("request_token")
    @Expose
    val requestToken: String
)
