package com.peacecodetech.medeli.data.model

data class Categories(
    val name: String?=null,
    val color: Int=0,
    val products: Products?=null
)

data class Products(
    val name: String,
    val img_url: String,
    var price: Int,
    var quantity: Int,
    val isFavorited: Boolean
)
