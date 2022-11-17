package com.peacecodetech.medeli.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "uid") val uid: String? = null,
    @ColumnInfo(name = "fullName") val fullName: String? = null,
    @ColumnInfo(name = "firstName") val firstName: String? = null,
    @ColumnInfo(name = "lastName") val lastName: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "password") val password: String? = null,
    @ColumnInfo(name = "imgUrl") val imgUrl: String? = null,
)
