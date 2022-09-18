package com.example.products

import com.example.getMockDb
import com.example.models.Categories
import com.example.models.Category
import com.example.models.Product
import com.example.models.Products
import com.example.products.dto.ProductDetails
import com.example.products.dto.ProductOnList
import com.example.services.ProductServiceImpl
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.stopKoin
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductTest {

    @BeforeTest
    fun stop() {
        stopKoin()
    }

    @Test
    fun testEmptyProductList() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)
            val productService = ProductServiceImpl(mockDb)
            val productList = productService.getProductsList()
            assertEquals(listOf(), productList)
        }
    }

    @Test
    fun testProductListWithProducts() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)
            SchemaUtils.create(Categories)
            var newCategory = Category.new { name = "test" }

            val product = Product.new { name = "Test"; price = 123; category = newCategory.id }

            val productService = ProductServiceImpl(mockDb)
            val productList = productService.getProductsList()
            assertEquals(listOf(ProductOnList(name = product.name, price = product.price)), productList)
        }
    }

    @Test
    fun testProductFind() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)
            SchemaUtils.create(Categories)
            var newCategory = Category.new { name = "test" }

            val product = Product.new { name = "Test"; price = 123; category = newCategory.id }
            Product.new { name = "Test2"; price = 444; category = newCategory.id }

            val productService = ProductServiceImpl(mockDb)
            val productList = productService.getProduct(product.id.value)
            assertEquals(ProductDetails(product), productList)
        }
    }

    @Test
    fun testProductFindWithoutProduct() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)

            val productService = ProductServiceImpl(mockDb)
            val productList = productService.getProduct(1)
            assertEquals(null, productList)
        }
    }
}
