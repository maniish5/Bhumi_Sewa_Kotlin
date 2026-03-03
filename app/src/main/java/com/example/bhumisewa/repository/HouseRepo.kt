package com.example.bhumisewa.repository

import com.example.bhumisewa.model.House

interface HouseRepo {
    fun getAllHouses(onResult: (Boolean, String, List<House>) -> Unit)
    fun addHouse(house: House, onResult: (Boolean, String) -> Unit)
    fun updateHouse(house: House, onResult: (Boolean, String) -> Unit)
    fun deleteHouse(houseId: String, onResult: (Boolean, String) -> Unit)
}