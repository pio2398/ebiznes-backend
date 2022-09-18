package com.example.datebase

import com.example.models.Categories
import com.example.models.Category
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryTest {
    @BeforeTest
    fun prepareDataBase() {
        Database.connect("jdbc:h2:mem:regular;", "org.h2.Driver")
    }

    @Test
    fun addRootCategory() {
        transaction {
            var newCategory: Category? = null

            SchemaUtils.create(Categories)

            newCategory = Category.new { name = "test" }

            assertEquals(Categories.selectAll().count(), 1)
            val categoryResult = Category.findById(newCategory!!.id)
            assertEquals(categoryResult!!.name, "test")
            assertEquals(categoryResult.id, newCategory!!.id)
        }
    }

    @Test
    fun addSubCategory() {
        transaction {
            SchemaUtils.create(Categories)
            // Add root category
            val newCategory = Category.new { name = "root" }

            val newSubCategory = Category.new {
                name = "child"; parent = newCategory
            }

            val categoryResult = Category.findById(newSubCategory!!.id)

            assertEquals(categoryResult!!.parent!!.name, "root")
            assertEquals(categoryResult.name, "child")
        }
    }
}
