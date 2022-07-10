package com.example.services

import com.example.models.Product
import com.example.products.dto.ProductList
import com.example.products.dto.ProductOnList
import org.jetbrains.exposed.sql.transactions.transaction

interface ProductService {
    fun getProductsList(): ProductList
}

class ProductServiceImpl(private val databaseFactory: DatabaseFactory) : ProductService {
    override fun getProductsList(): ProductList = transaction(databaseFactory.dataBase) {
        var products = Product.all().map { ProductOnList(it) }
        ProductList(products)
    }
}
