package com.example.bhumisewa.model

data class SavedItem(
    val houseId: String = "",
    val title: String = "",
    val address: String = "",
    val city: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val type: String = "",
    val bedrooms: Int = 1,
    val rating: Float = 0f
)