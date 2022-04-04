package com.masliaiev.filmspace.domain.entity

data class AccountStates(
    val id: Int,
    val favorite: Boolean,
    val rated: Double?,
    val watchlist: Boolean
)
