package com.masliaiev.filmspace.data.network.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteSessionRequestDto(
    @SerializedName("session_id")
    @Expose
    val sessionId: String
)
