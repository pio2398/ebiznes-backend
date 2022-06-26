package com.example.dao

import com.example.models.User
import com.example.models.Users
import com.example.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        token = row[Users.token],
        username = row[Users.username],
        vendorId = row[Users.vendorId],
    )
    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun user(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addUser(username: String, token: String, vendorId: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.token] = token
            it[Users.username] = username
            it[Users.vendorId] = vendorId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun getOrCreateUser(username: String, token: String, vendorId: String): User?  {
        val result: User? = dbQuery {
            Users
                .select { Users.vendorId eq vendorId }
                .map(::resultRowToUser)
                .singleOrNull()
        } ?: return this.addUser(username, token,vendorId)
        println("Return existing user.")
        return result;
    }
}