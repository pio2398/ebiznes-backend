package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object ProductsInOrder : Table() {
    var product = reference("product", Products)
    var user = reference("user", Products)
    override val primaryKey = PrimaryKey(product, user)
}

object Orders : IntIdTable() {
    var user = reference("user", Users)
}

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)
    var user by Orders.user

    var product by Product via ProductsInOrder
}
