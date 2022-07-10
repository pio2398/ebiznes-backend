package com.example.services

import com.example.models.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

interface DatabaseFactory {
    fun init()
}

class DatabaseFactoryImpl : DatabaseFactory {
    override fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Attributes)
            SchemaUtils.create(Categories)
            SchemaUtils.create(Orders)
            SchemaUtils.create(Products)
            SchemaUtils.create(UserCarts)
            SchemaUtils.create(CategoriesAttributes)
            SchemaUtils.create(ProductsInOrder)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

val koinDbModule = module {
    singleOf(::DatabaseFactoryImpl) { bind<DatabaseFactory>() }
}
