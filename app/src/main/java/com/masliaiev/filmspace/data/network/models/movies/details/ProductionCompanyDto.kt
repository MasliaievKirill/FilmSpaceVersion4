package com.masliaiev.filmspace.data.network.models.movies.details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductionCompanyDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("logo_path")
    @Expose
    val logoPath: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("origin_country")
    @Expose
    val originCountry: String
)
