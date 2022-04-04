package com.masliaiev.filmspace.data.network.models.movies

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.masliaiev.filmspace.data.network.models.movies.MovieDto

data class MoviesListDto(
    @SerializedName("page")
    @Expose
    val page: Int,
    @SerializedName("results")
    @Expose
    val results: List<MovieDto>?,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int,
    @SerializedName("total_results")
    @Expose
    val totalResults: Int
)
