package com.example.bhumisewa.model

data class House(
    val houseId: String = "",
    val title: String = "",
    val description: String = "",
    val address: String = "",
    val city: String = "",
    val district: String = "",
    val type: String = "Apartment",       // Apartment, House, Studio, Villa
    val bedrooms: Int = 1,
    val bathrooms: Int = 1,
    val area: String = "",                // e.g. "850 sq ft"
    val floor: String = "",              // e.g. "3rd Floor"
    val furnished: String = "Furnished", // Furnished, Semi-Furnished, Unfurnished
    val price: String = "",              // e.g. "Rs. 25,000/month"
    val priceValue: Long = 0L,
    val depositAmount: String = "",
    val availableFrom: String = "",
    val imageUrl: String = "",
    val imageUrls: List<String> = emptyList(),
    val amenities: List<String> = emptyList(), // WiFi, Parking, Gym, etc.
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val isFeatured: Boolean = false,
    val isAvailable: Boolean = true,
    val ownerName: String = "",
    val ownerPhone: String = "",
    val ownerEmail: String = "",
    val createdAt: Long = 0L
)