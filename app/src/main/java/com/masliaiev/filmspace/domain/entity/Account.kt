package com.masliaiev.filmspace.domain.entity

data class Account(
    val avatarPath: String,
    val id: Int,
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val includeAdult: Boolean,
    val username: String
)
