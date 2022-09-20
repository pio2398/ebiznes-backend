package com.example.datebase

import com.example.models.Categories
import com.example.models.Category
import com.example.models.Product
import com.example.models.Products
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductTest {
    @BeforeTest
    fun prepareDataBase() {
        Database.connect("jdbc:h2:mem:regular;", "org.h2.Driver")
    }

    @Test
    fun addNewProduct() {
        transaction {

            SchemaUtils.create(Products)
            SchemaUtils.create(Categories)

            var newCategory = Category.new { name = "test" }

            val newProduct = Product.new { name = "test"; price = 1234; category = newCategory.id }
            assertEquals(Products.selectAll().count(), 1)
            val categoryResult = Product.findById(newProduct.id)
            assertEquals(categoryResult!!.name, "test")
            assertEquals(categoryResult.id, newProduct.id)
        }
    }
}
