package com.masliaiev.filmspace.data.network.models.movies.details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpokenLanguageDto(
    @SerializedName("english_name")
    @Expose
    val englishName: String,
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String,
    @SerializedName("name")
    @Expose
    val name: String
)
