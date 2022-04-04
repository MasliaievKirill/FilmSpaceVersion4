package com.masliaiev.filmspace.domain.entity

data class Video(
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String,
    val id: String
)
