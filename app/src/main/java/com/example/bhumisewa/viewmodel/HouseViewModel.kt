package com.example.bhumisewa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bhumisewa.model.House
import com.example.bhumisewa.repository.HouseRepoImpl

class HouseViewModel : ViewModel() {
    private val repo = HouseRepoImpl()

    val houses = mutableStateListOf<House>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val apartments  get() = houses.filter { it.type == "Apartment" }
    val villas      get() = houses.filter { it.type == "Villa" }
    val studios     get() = houses.filter { it.type == "Studio" }
    val featured    get() = houses.filter { it.isFeatured }
    val available   get() = houses.filter { it.isAvailable }

    fun loadHouses() {
        isLoading = true
        repo.getAllHouses { success, msg, list ->
            isLoading = false
            if (success) {
                houses.clear()
                houses.addAll(list)
            } else {
                errorMessage = msg
            }
        }
    }

    fun addHouse(house: House, onResult: (Boolean, String) -> Unit) =
        repo.addHouse(house, onResult)

    fun updateHouse(house: House, onResult: (Boolean, String) -> Unit) =
        repo.updateHouse(house, onResult)

    fun deleteHouse(houseId: String, onResult: (Boolean, String) -> Unit) =
        repo.deleteHouse(houseId, onResult)
}