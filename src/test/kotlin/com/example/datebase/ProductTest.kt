package com.example.datebase

import com.example.models.Product
import com.example.models.Products
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*
import kotlin.test.assertEquals

class ProductTest {
    @BeforeTest
    fun prepareDataBase() {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    @Test
    fun addRootCategory() {
        transaction {
            SchemaUtils.create(Products)
            val newCategory = Product.new { name = "test"; price = 1234.0 }
            assertEquals(Products.selectAll().count(), 1)
            val categoryResult = Product.findById(newCategory.id)
            assertEquals(categoryResult!!.name, "test")
            assertEquals(categoryResult.id, newCategory.id)
        }
    }
}
