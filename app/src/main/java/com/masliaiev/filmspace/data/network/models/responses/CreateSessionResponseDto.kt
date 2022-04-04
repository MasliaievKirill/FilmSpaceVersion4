package com.masliaiev.filmspace.data.network.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateSessionResponseDto(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("session_id")
    @Expose
    val sessionId: String
)
