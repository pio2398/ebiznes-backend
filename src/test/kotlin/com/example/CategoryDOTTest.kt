package com.example

import com.example.dao.category.CategoryDAOImpl
import com.example.models.Categories
import com.example.models.Category
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*
import kotlin.test.assertEquals

class CategoryDOTTest {
    @BeforeTest
    fun prepareDataBase() {
        val database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
        database.connector.invoke() // Keep a connection open so the DB is not destroyed after the first transaction
    }

    @Test
    fun addRootCategory() {
        transaction {
            SchemaUtils.create(Categories)
            val newCategory = CategoryDAOImpl().add("test", null)
            assertEquals(Categories.selectAll().count(), 1)
            val categoryResult = Category.findById(newCategory!!.id)
            assertEquals(categoryResult!!.name, "test")
            assertEquals(categoryResult.id, newCategory.id)
        }
    }

    @Test
    fun addSubCategory() {
        transaction {
            SchemaUtils.create(Categories)
            // Add root category
            val newCategory = CategoryDAOImpl().add("root", null)

            val newSubCategory = CategoryDAOImpl().add("child", newCategory)
            assertEquals(Categories.selectAll().count(), 2)

            val categoryResult = Category.findById(newSubCategory!!.id)

            assertEquals(categoryResult!!.parent!!.name, "root")
            assertEquals(categoryResult!!.name, "child")
        }
    }
}
