package com.example.bhumisewa.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bhumisewa.repository.AuthRepoImpl
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val repo = AuthRepoImpl()

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) =
        repo.login(email, password, onResult)

    fun register(fullName: String, email: String, password: String, onResult: (Boolean, String) -> Unit) =
        repo.register(fullName, email, password, onResult)

    fun logout() = repo.logout()

    fun isLoggedIn() = FirebaseAuth.getInstance().currentUser != null
    fun currentUserId() = repo.currentUserId()
    fun currentUserName() = repo.currentUserName() ?: "Guest"
    fun currentUserEmail() = repo.currentUserEmail() ?: ""
}