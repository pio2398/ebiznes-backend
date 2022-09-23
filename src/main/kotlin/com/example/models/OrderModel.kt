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
    var total_cost = integer("price")
    val paid = bool("paid").default(false)
    val payment_client_secret = text("payment_client_secret").nullable()
    val ts = text("ts").nullable()
}

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)

    var user by Orders.user
    var product by Product via ProductsInOrder
    var paid by Orders.paid
    var payment_client_secret by Orders.payment_client_secret
    var ts by Orders.ts
    var total_cost by Orders.total_cost

}
