package com.example.products
import com.example.getMockDb
import com.example.models.*
import com.example.products.dto.ProductDetails
import com.example.products.dto.ProductList
import com.example.products.dto.ProductOnList
import com.example.services.ProductServiceImpl
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.stopKoin
import kotlin.test.*

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
            assertEquals(ProductList(listOf()), productList)
        }
    }

    @Test
    fun testProductListWithProducts() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)
            val product = Product.new { name = "Test"; price = 123.4 }

            val productService = ProductServiceImpl(mockDb)
            val productList = productService.getProductsList()
            assertEquals(ProductList(listOf(ProductOnList(name = product.name, price = product.price))), productList)
        }
    }

    @Test
    fun testProductFind() {
        val mockDb = getMockDb()

        transaction(mockDb.dataBase) {
            SchemaUtils.create(Products)
            val product = Product.new { name = "Test"; price = 123.4 }
            Product.new { name = "Test2"; price = 444.4 }

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
