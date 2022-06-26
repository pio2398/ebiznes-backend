package com.example.dao

import com.example.models.User
import io.ktor.utils.io.core.*


interface DAOFacade {
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addUser(username: String, token: String, vendorId: String): User?
    suspend fun getOrCreateUser(username: String, token: String, vendorId: String) : User?
}