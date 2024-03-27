package com.imaginato.homeworkmvvm.data.local.login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppUsers")
data class AppUser(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "UserName") var userName: String,
    @ColumnInfo(name = "isDeleted") var isDeleted: Boolean,
    @ColumnInfo(name = "Token") var token: String,
)
