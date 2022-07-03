package com.example.dao

import com.example.models.User
import com.example.models.Users
import org.jetbrains.exposed.sql.*

class UserDAOImpl : UserDAO {
    override suspend fun allUsers(): SizedIterable<User> = User.all()

    override suspend fun user(id: Int): User? = User.findById(id = id)

    override suspend fun addUser(username: String, token: String, vendorId: String): User? {
        return User.new {
            this.username = username
            this.token = token
            this.vendorId = vendorId
        }
    }

    override suspend fun getOrCreateUser(username: String, token: String, vendorId: String): User {
        return User.find { Users.vendorId eq vendorId }.firstOrNull() ?: this.addUser(username, token, vendorId)!!
    }
}
