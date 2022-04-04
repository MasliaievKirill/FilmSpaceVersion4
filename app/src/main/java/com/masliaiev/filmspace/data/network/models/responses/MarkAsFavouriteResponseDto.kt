package com.masliaiev.filmspace.data.network.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MarkAsFavouriteResponseDto(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int,
    @SerializedName("status_message")
    @Expose
    val statusMassage: String
)
