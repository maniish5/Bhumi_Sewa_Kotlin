package com.example.bhumisewa.repository

interface AuthRepo {
    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit)
    fun register(fullName: String, email: String, password: String, onResult: (Boolean, String) -> Unit)
    fun logout()
    fun currentUserId(): String?
    fun currentUserEmail(): String?
    fun currentUserName(): String?
}