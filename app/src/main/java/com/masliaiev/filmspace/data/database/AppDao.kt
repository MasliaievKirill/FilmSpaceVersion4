package com.masliaiev.filmspace.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.masliaiev.filmspace.data.database.models.AccountDbModel
import com.masliaiev.filmspace.data.database.models.GenreDbModel

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(accountDbModel: AccountDbModel)

    @Query("SELECT * FROM account LIMIT 1")
    suspend fun getAccount(): AccountDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(genresList: List<GenreDbModel>)

    @Query("SELECT * FROM genres WHERE id = :id LIMIT 1")
    suspend fun getGenre(id: Int): GenreDbModel


}