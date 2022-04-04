package com.masliaiev.filmspace.data.network.models.movies.videos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoListDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("results")
    @Expose
    val results: List<VideoDto>?
)
