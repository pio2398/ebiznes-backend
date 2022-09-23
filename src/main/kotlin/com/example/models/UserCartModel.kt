package com.example.models

import org.jetbrains.exposed.sql.Table

object UserCarts : Table() {
    val user = reference("user", Users)
    val product = reference("product", Products)
    override val primaryKey = PrimaryKey(user, product)
}