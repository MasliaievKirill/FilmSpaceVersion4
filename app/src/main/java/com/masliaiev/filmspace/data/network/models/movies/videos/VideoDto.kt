package com.masliaiev.filmspace.data.network.models.movies.videos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String,
    @SerializedName("iso_3166_1")
    @Expose
    val iso31661: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("key")
    @Expose
    val key: String,
    @SerializedName("site")
    @Expose
    val site: String,
    @SerializedName("size")
    @Expose
    val size: Int,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("official")
    @Expose
    val official: Boolean,
    @SerializedName("published_at")
    @Expose
    val publishedAt: String,
    @SerializedName("id")
    @Expose
    val id: String
)
