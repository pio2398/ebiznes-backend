package com.example.services

import com.example.models.Product
import com.example.products.dto.ProductDetails
import com.example.products.dto.ProductList
import com.example.products.dto.ProductOnList
import org.jetbrains.exposed.sql.transactions.transaction

interface ProductService {
    fun getProductsList(): ProductList
    fun getProduct(product_id: Int): ProductDetails?
}

class ProductServiceImpl(private val databaseFactory: DatabaseFactory) : ProductService {
    override fun getProductsList(): ProductList = transaction(databaseFactory.dataBase) {
        val products = Product.all().map { ProductOnList(it) }
        ProductList(products)
    }

    override fun getProduct(product_id: Int): ProductDetails? {
        val product = Product.findById(product_id)
        return if (product == null) null else ProductDetails(product)
    }
}
