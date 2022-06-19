package com.example.models

import org.jetbrains.exposed.sql.*

data class User(val id: Int, val username: String, val token: String, val vendorId: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("name", 1024);
    val token = varchar("token", 1024);
    val vendorId = varchar("vendor_id", 1024);


    override val primaryKey = PrimaryKey(id)
}