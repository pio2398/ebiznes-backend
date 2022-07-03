package com.example.dao

import com.example.models.User
import org.jetbrains.exposed.sql.SizedIterable

interface UserDAO {
    suspend fun allUsers(): SizedIterable<User>
    suspend fun user(id: Int): User?
    suspend fun addUser(username: String, token: String, vendorId: String): User?
    suspend fun getOrCreateUser(username: String, token: String, vendorId: String): User?
}
