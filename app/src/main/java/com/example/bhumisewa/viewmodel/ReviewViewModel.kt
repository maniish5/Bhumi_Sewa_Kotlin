package com.example.bhumisewa.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bhumisewa.model.Review
import com.example.bhumisewa.repository.ReviewRepoImpl

class ReviewViewModel : ViewModel() {
    private val repo = ReviewRepoImpl()

    val reviews = mutableStateListOf<Review>()
    var isLoading by mutableStateOf(false)

    fun loadReviews(houseId: String) {
        isLoading = true
        repo.getHouseReviews(houseId) { _, _, list ->
            isLoading = false
            reviews.clear()
            reviews.addAll(list)
        }
    }

    fun addReview(review: Review, onResult: (Boolean, String) -> Unit) =
        repo.addReview(review, onResult)

    fun deleteReview(reviewId: String, houseId: String, onResult: (Boolean, String) -> Unit) =
        repo.deleteReview(reviewId, houseId, onResult)

    fun averageRating(): Float {
        if (reviews.isEmpty()) return 0f
        return reviews.map { it.rating }.average().toFloat()
    }
}