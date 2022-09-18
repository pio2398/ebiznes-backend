package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Products : IntIdTable() {
    val name = varchar("name", 254)
    val description = text("description").nullable()
    val price = integer("price")
    val picture_url = text("picture_url").nullable()
    val category = reference("category", Categories)

}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var name by Products.name
    var description by Products.description
    var price by Products.price
    var picture_url by Products.picture_url
    var category by Products.category
}
