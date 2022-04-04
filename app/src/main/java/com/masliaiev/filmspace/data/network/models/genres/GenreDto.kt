package com.masliaiev.filmspace.data.network.models.genres

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String
)