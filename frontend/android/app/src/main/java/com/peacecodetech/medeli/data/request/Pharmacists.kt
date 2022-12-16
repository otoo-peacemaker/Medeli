package com.peacecodetech.medeli.data.request

import androidx.room.ColumnInfo
import com.peacecodetech.medeli.data.responses.Products

data class Pharmacists(
    val id:Int,
    val name:String,
    val email:String,
    val phone:String,
    val address:String,
    val logo: String? = null,
    val distance: String? = null,
    val description: String? = null,
    val rating: Int? = null,
    val products: Products,
    val riders: Riders
)
