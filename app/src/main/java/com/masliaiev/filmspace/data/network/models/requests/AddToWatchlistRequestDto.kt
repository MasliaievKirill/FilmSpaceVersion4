package com.masliaiev.filmspace.data.network.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddToWatchlistRequestDto(
    @SerializedName("media_type")
    @Expose
    val mediaType: String,
    @SerializedName("media_id")
    @Expose
    val mediaId: Int,
    @SerializedName("watchlist")
    @Expose
    val favorite: Boolean
)
