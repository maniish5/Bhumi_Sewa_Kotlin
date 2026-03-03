package com.example.bhumisewa.repository

import com.example.bhumisewa.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReviewRepoImpl : ReviewRepo {
    private val db = Firebase.database.reference.child("reviews")
    private val auth = FirebaseAuth.getInstance()

    override fun addReview(review: Review, onResult: (Boolean, String) -> Unit) {
        val id = db.child(review.houseId).push().key ?: return onResult(false, "Failed to generate ID")
        val uid = auth.currentUser?.uid ?: ""
        val name = auth.currentUser?.displayName ?: "Anonymous"
        val r = review.copy(
            reviewId = id,
            userId = uid,
            userName = name,
            createdAt = System.currentTimeMillis()
        )
        db.child(review.houseId).child(id).setValue(r)
            .addOnSuccessListener { onResult(true, "Review submitted") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun getHouseReviews(houseId: String, onResult: (Boolean, String, List<Review>) -> Unit) {
        db.child(houseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                    .sortedByDescending { it.createdAt }
                onResult(true, "OK", list)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(false, error.message, emptyList())
            }
        })
    }

    override fun deleteReview(reviewId: String, houseId: String, onResult: (Boolean, String) -> Unit) {
        db.child(houseId).child(reviewId).removeValue()
            .addOnSuccessListener { onResult(true, "Review deleted") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }
}