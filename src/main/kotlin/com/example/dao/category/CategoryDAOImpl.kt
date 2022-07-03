package com.example.dao.category

import com.example.models.Category
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryDAOImpl : CategoryDAO {
    override fun add(name: String, parent: Category?): Category? {
        return transaction {
            Category.new {
                this.name = name
                this.parent = parent
            }
        }
    }
}
