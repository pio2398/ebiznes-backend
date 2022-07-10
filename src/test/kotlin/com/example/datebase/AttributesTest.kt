package com.example.datebase

import com.example.models.Attribute
import com.example.models.Attributes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class AttributesTest {
    @BeforeTest
    fun prepareDataBase() {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    @Test
    fun addAttributes() {
        transaction {
            SchemaUtils.create(Attributes)
            val newAttribute = Attribute.new { name = "test" }
            assertEquals(Attributes.selectAll().count(), 1)
            val resultAttribute = Attribute.findById(newAttribute!!.id)
            assertEquals(resultAttribute!!.name, "test")
            assertEquals(resultAttribute.id, newAttribute.id)
        }
    }
}
