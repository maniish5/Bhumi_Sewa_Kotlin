package com.example.bhumisewa.repository

import com.example.bhumisewa.model.SavedItem

interface SavedRepo {
    fun addToSaved(item: SavedItem, onResult: (Boolean, String) -> Unit)
    fun removeFromSaved(houseId: String, onResult: (Boolean, String) -> Unit)
    fun getUserSaved(userId: String, onResult: (Boolean, String, List<SavedItem>) -> Unit)
    fun isSaved(houseId: String, userId: String, onResult: (Boolean) -> Unit)
}