package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Attributes : IntIdTable() {
    val name = varchar("name", 254)
    val type = varchar("type", 254).nullable()
}

class Attribute(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Attribute>(Attributes)

    var name by Attributes.name
    var type by Attributes.type
}
