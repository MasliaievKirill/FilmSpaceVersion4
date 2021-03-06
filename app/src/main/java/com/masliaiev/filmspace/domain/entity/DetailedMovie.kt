package com.masliaiev.filmspace.domain.entity

data class DetailedMovie(
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val genres: String,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val revenue: Int,
    val runtime: String?,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: String,
    val voteCount: Int
)
