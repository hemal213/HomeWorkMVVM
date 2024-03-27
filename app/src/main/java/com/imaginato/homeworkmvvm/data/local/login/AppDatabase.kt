package com.imaginato.homeworkmvvm.data.local.login

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppUser::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract val appDao: AppDBDao
}