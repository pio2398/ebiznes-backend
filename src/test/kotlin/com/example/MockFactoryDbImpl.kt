package com.example

import com.example.services.DatabaseFactory
import org.jetbrains.exposed.sql.Database

class MockFactoryDbImpl(override val dataBase: Database) : DatabaseFactory

fun getMockDb(): DatabaseFactory {
    val db = Database.connect("jdbc:h2:mem:regular;")
    return MockFactoryDbImpl(db)
}
