package com.masliaiev.filmspace.data.network.models.movies.details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BelongsToCollectionDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String,
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String
)
