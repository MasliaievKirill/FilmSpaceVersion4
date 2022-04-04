package com.masliaiev.filmspace.data.network.models.movies.account_states

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatedDto(
    @SerializedName("value")
    @Expose
    val value: Double
)
