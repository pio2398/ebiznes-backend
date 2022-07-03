package com.example.services

import com.example.dao.category.CategoryDAO
import com.example.dao.product.ProductDAO

interface DebugService {
    fun debug(): String
}

class DebugServiceImpl(private val productDAO: ProductDAO, private val categoryDAO: CategoryDAO) : DebugService {
    override fun debug(): String {
        categoryDAO.add("test", null)
        return "OK"
    }
}
