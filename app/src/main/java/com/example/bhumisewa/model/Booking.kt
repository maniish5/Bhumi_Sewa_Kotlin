package com.example.bhumisewa.model

data class Booking(
    val bookingId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val houseId: String = "",
    val houseTitle: String = "",
    val houseAddress: String = "",
    val houseImageUrl: String = "",
    val housePrice: String = "",
    val tenantName: String = "",
    val tenantPhone: String = "",
    val tenantEmail: String = "",
    val moveInDate: String = "",
    val duration: String = "",            // e.g. "6 months", "1 year"
    val message: String = "",
    val status: String = "Pending",       // Pending, Confirmed, Rejected, Cancelled
    val bookedAt: Long = 0L
)