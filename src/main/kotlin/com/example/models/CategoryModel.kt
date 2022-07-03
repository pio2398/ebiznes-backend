package com.example.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object CategoriesAttributes : Table() {
    val category = reference("category", Categories)
    val attribute = reference("attribute", Attributes)
    override val primaryKey = PrimaryKey(
        category, attribute,
    )
}

object Categories : IntIdTable() {
    val name = varchar("name", 254)
    val parent = reference("parent", Categories).nullable()
}

class Category(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Category>(Categories)
    var name by Categories.name
    var parent by Category optionalReferencedOn Categories.parent
    var attributes by Attribute via CategoriesAttributes
}
