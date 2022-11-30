package com.peacecodetech.medeli.model

data class Categories(
    val name: String?=null,
    val color: Int=0,
    val products: Products?=null
)

data class Products(
    val name: String,
    val img_url: String,
    val price: Double,
    val quantity: Int,
    val isFavorited: Boolean
)
