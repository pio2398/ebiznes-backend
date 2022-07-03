package com.example.models

import org.jetbrains.exposed.sql.Table

object UserCarts : Table() {
    var user = reference("user", Users)
    var product = reference("product", Products)
    override val primaryKey = PrimaryKey(user, product)
}
