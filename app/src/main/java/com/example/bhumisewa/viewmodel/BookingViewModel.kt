package com.example.bhumisewa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bhumisewa.model.Booking
import com.example.bhumisewa.repository.BookingRepoImpl
import com.google.firebase.auth.FirebaseAuth

class BookingViewModel : ViewModel() {
    private val repo = BookingRepoImpl()

    val userBookings = mutableStateListOf<Booking>()
    val allBookings  = mutableStateListOf<Booking>()
    var isLoading by mutableStateOf(false)

    fun placeBooking(booking: Booking, onResult: (Boolean, String) -> Unit) =
        repo.createBooking(booking, onResult)

    fun loadUserBookings() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        isLoading = true
        repo.getUserBookings(uid) { _, _, list ->
            isLoading = false
            userBookings.clear()
            userBookings.addAll(list)
        }
    }

    fun loadAllBookings() {
        isLoading = true
        repo.getAllBookings { _, _, list ->
            isLoading = false
            allBookings.clear()
            allBookings.addAll(list)
        }
    }

    fun updateStatus(bookingId: String, status: String, onResult: (Boolean, String) -> Unit) =
        repo.updateBookingStatus(bookingId, status, onResult)
}