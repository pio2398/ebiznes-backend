package com.example.products
import com.example.MockFactoryDbImpl
import com.example.models.*
import com.example.products.dto.ProductList
import com.example.services.DatabaseFactory
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.sql.DriverManager
import kotlin.test.*

class ProductTest : KoinTest {
    @BeforeTest
    fun prepareDataBase() {
        val sqlitePath = "jdbc:sqlite:file:test?mode=memory&cache=shared"
        val keepAliveConnection = DriverManager.getConnection(sqlitePath)
        val db = Database.connect(sqlitePath)
        startKoin {}
        loadKoinModules(module { singleOf(::MockFactoryDbImpl) { bind<DatabaseFactory>() } })
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun testEmptyProductList() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        application {
            configureRouting()
        }

        val productList: ProductList = client.get("/products").body()
        assertEquals(ProductList(listOf()), productList)
    }

    @Test
    fun testProductListWithProducts() = transaction {
        testApplication {
            application {
                configureRouting()
            }
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            SchemaUtils.create(Products)
            val sqlitePath = "jdbc:sqlite:file:test?mode=memory&cache=shared"
            val keepAliveConnection = DriverManager.getConnection(sqlitePath)
            val db = Database.connect(sqlitePath)

            var product: Product? = null
            product = Product.new { name = "Test"; price = 123.4 }
            product = Product.new { name = "Test"; price = 123.4 }
            product = Product.new { name = "Test"; price = 123.4 }
            product = Product.new { name = "Test"; price = 123.4 }
            product = Product.new { name = "Test"; price = 123.4 }
            product = Product.new { name = "Test"; price = 123.4 }

            Product.all().count()

            print(product!!.id)

            val productList: ProductList = client.get("/products").body()
            assertNotEquals(ProductList(listOf()), productList)
            print(product!!.id)
        }
    }
}
