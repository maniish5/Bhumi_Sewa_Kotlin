package com.example.bhumisewa.utils

import androidx.compose.runtime.mutableStateListOf
import com.example.bhumisewa.model.House
import com.example.bhumisewa.model.SavedItem

object SavedManager {
    val items = mutableStateListOf<SavedItem>()

    fun isSaved(houseId: String) = items.any { it.houseId == houseId }

    fun toggle(house: House) {
        val existing = items.find { it.houseId == house.houseId }
        if (existing != null) {
            items.remove(existing)
        } else {
            items.add(
                SavedItem(
                    houseId = house.houseId,
                    title = house.title,
                    address = house.address,
                    city = house.city,
                    price = house.price,
                    imageUrl = house.imageUrl,
                    type = house.type,
                    bedrooms = house.bedrooms,
                    rating = house.rating
                )
            )
        }
    }
}