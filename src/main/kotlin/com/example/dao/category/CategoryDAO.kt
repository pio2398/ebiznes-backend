package com.example.dao.category

import com.example.models.Category

interface CategoryDAO {
    fun add(name: String, parent: Category?): Category?
}
