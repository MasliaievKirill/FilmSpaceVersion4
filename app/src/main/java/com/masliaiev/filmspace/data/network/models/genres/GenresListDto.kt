package com.masliaiev.filmspace.data.network.models.genres

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.masliaiev.filmspace.domain.entity.Genre

data class GenresListDto(
    @SerializedName("genres")
    @Expose
    val genresList: List<GenreDto>
)
