package com.masliaiev.filmspace.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountDbModel(
    val avatarPath: String,
    @PrimaryKey
    val id: Int,
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val includeAdult: Boolean,
    val username: String
)
