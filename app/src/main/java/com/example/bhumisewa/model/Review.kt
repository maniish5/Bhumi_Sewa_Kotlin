package com.example.bhumisewa.model

data class Review(
    val reviewId: String = "",
    val houseId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 5f,
    val comment: String = "",
    val createdAt: Long = 0L
)