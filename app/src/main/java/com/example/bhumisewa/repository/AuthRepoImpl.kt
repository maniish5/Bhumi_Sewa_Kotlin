package com.example.bhumisewa.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthRepoImpl : AuthRepo {
    private val auth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val name = result.user?.displayName ?: email.substringBefore("@")
                onResult(true, name)
            }
            .addOnFailureListener { onResult(false, it.message ?: "Login failed") }
    }

    override fun register(fullName: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName).build()
                result.user?.updateProfile(profileUpdate)
                onResult(true, fullName)
            }
            .addOnFailureListener { onResult(false, it.message ?: "Registration failed") }
    }

    override fun logout() = auth.signOut()
    override fun currentUserId() = auth.currentUser?.uid
    override fun currentUserEmail() = auth.currentUser?.email
    override fun currentUserName() = auth.currentUser?.displayName
}