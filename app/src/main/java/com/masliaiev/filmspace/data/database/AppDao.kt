package com.masliaiev.filmspace.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.masliaiev.filmspace.data.database.models.AccountDbModel

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(accountDbModel: AccountDbModel)

    @Query("SELECT * FROM account LIMIT 1")
    suspend fun getAccount(): AccountDbModel

}