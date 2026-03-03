package com.example.bhumisewa.repository

import com.example.bhumisewa.model.Review

interface ReviewRepo {
    fun addReview(review: Review, onResult: (Boolean, String) -> Unit)
    fun getHouseReviews(houseId: String, onResult: (Boolean, String, List<Review>) -> Unit)
    fun deleteReview(reviewId: String, houseId: String, onResult: (Boolean, String) -> Unit)
}