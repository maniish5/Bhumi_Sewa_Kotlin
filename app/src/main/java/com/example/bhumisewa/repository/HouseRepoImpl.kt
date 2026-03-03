package com.example.bhumisewa.repository

import com.example.bhumisewa.model.House
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HouseRepoImpl : HouseRepo {
    private val db = Firebase.database.reference.child("houses")

    override fun getAllHouses(onResult: (Boolean, String, List<House>) -> Unit) {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(House::class.java) }
                onResult(true, "OK", list)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(false, error.message, emptyList())
            }
        })
    }

    override fun addHouse(house: House, onResult: (Boolean, String) -> Unit) {
        val id = db.push().key ?: return onResult(false, "Failed to generate ID")
        val h = house.copy(houseId = id, createdAt = System.currentTimeMillis())
        db.child(id).setValue(h)
            .addOnSuccessListener { onResult(true, "House listed successfully") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun updateHouse(house: House, onResult: (Boolean, String) -> Unit) {
        db.child(house.houseId).setValue(house)
            .addOnSuccessListener { onResult(true, "Updated") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun deleteHouse(houseId: String, onResult: (Boolean, String) -> Unit) {
        db.child(houseId).removeValue()
            .addOnSuccessListener { onResult(true, "Listing removed") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }
}