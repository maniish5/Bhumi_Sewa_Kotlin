package com.example.bhumisewa.repository

import com.example.bhumisewa.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookingRepoImpl : BookingRepo {
    private val db = Firebase.database.reference.child("bookings")
    private val auth = FirebaseAuth.getInstance()

    override fun createBooking(booking: Booking, onResult: (Boolean, String) -> Unit) {
        val id = db.push().key ?: return onResult(false, "Failed to generate ID")
        val uid = auth.currentUser?.uid ?: ""
        val email = auth.currentUser?.email ?: ""
        val name = auth.currentUser?.displayName ?: ""
        val b = booking.copy(
            bookingId = id,
            userId = uid,
            userEmail = email,
            userName = name,
            bookedAt = System.currentTimeMillis()
        )
        db.child(id).setValue(b)
            .addOnSuccessListener { onResult(true, "Booking request sent! ID: $id") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun getUserBookings(userId: String, onResult: (Boolean, String, List<Booking>) -> Unit) {
        db.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Booking::class.java) }
                        .sortedByDescending { it.bookedAt }
                    onResult(true, "OK", list)
                }
                override fun onCancelled(error: DatabaseError) {
                    onResult(false, error.message, emptyList())
                }
            })
    }

    override fun getAllBookings(onResult: (Boolean, String, List<Booking>) -> Unit) {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Booking::class.java) }
                    .sortedByDescending { it.bookedAt }
                onResult(true, "OK", list)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(false, error.message, emptyList())
            }
        })
    }

    override fun updateBookingStatus(bookingId: String, status: String, onResult: (Boolean, String) -> Unit) {
        db.child(bookingId).child("status").setValue(status)
            .addOnSuccessListener { onResult(true, "Status updated to $status") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }
}