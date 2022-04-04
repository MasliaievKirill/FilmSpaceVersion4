package com.masliaiev.filmspace.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreDbModel(
    @PrimaryKey
    val id: Int,
    val name: String
)