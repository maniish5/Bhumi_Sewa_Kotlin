package com.example.bhumisewa.repository

import com.example.bhumisewa.model.Booking

interface BookingRepo {
    fun createBooking(booking: Booking, onResult: (Boolean, String) -> Unit)
    fun getUserBookings(userId: String, onResult: (Boolean, String, List<Booking>) -> Unit)
    fun getAllBookings(onResult: (Boolean, String, List<Booking>) -> Unit)
    fun updateBookingStatus(bookingId: String, status: String, onResult: (Boolean, String) -> Unit)
}