package com.masliaiev.filmspace.data.network.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteSessionResponseDto(
    @SerializedName("success")
    @Expose
    val success: Boolean
)
