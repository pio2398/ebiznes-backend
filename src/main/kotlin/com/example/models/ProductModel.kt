package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

data class Product(val id: Int)

object Products : IntIdTable() {
//    val id = integer("id").autoIncrement()
    val name = varchar("name", 254)
    val description = text("description")


//    override val primaryKey = PrimaryKey(id)
}