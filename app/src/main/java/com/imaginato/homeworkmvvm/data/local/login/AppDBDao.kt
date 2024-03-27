package com.imaginato.homeworkmvvm.data.local.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface AppDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: AppUser)

    //TODO add other function to perform CRUD operations here for Room Database
}