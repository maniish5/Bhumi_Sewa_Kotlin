package com.example.bhumisewa.repository

import com.example.bhumisewa.model.SavedItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SavedRepoImpl : SavedRepo {
    private val auth = FirebaseAuth.getInstance()
    private fun userDb() = Firebase.database.reference
        .child("saved")
        .child(auth.currentUser?.uid ?: "guest")

    override fun addToSaved(item: SavedItem, onResult: (Boolean, String) -> Unit) {
        userDb().child(item.houseId).setValue(item)
            .addOnSuccessListener { onResult(true, "Saved successfully") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun removeFromSaved(houseId: String, onResult: (Boolean, String) -> Unit) {
        userDb().child(houseId).removeValue()
            .addOnSuccessListener { onResult(true, "Removed from saved") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed") }
    }

    override fun getUserSaved(userId: String, onResult: (Boolean, String, List<SavedItem>) -> Unit) {
        Firebase.database.reference.child("saved").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(SavedItem::class.java) }
                    onResult(true, "OK", list)
                }
                override fun onCancelled(error: DatabaseError) {
                    onResult(false, error.message, emptyList())
                }
            })
    }

    override fun isSaved(houseId: String, userId: String, onResult: (Boolean) -> Unit) {
        Firebase.database.reference.child("saved").child(userId).child(houseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) = onResult(snapshot.exists())
                override fun onCancelled(error: DatabaseError) = onResult(false)
            })
    }
}