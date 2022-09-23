package com.example.services

import com.example.exceptions.InvalidInputException
import com.example.models.Category
import com.example.models.Product
import com.example.models.Products
import com.example.products.dto.ProductDetails
import com.example.products.dto.ProductOnList
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

interface ProductService {
    fun getProductsList(): List<ProductOnList>
    fun getProduct(product_id: Int): ProductDetails?
    fun addProduct(productDetails: ProductDetails): ProductDetails


}

class ProductServiceImpl(private val databaseFactory: DatabaseFactory) : ProductService {
    override fun getProductsList(): List<ProductOnList> = transaction(databaseFactory.dataBase) {
        Product.all().map { ProductOnList(it) };
    }

    override fun getProduct(product_id: Int): ProductDetails? = transaction(databaseFactory.dataBase) {
        val product = Product.findById(product_id)
        if (product == null) null else ProductDetails(product)
    }

    override fun addProduct(productDetails: ProductDetails): ProductDetails = transaction(databaseFactory.dataBase) {
        val category =
            Category.findById(productDetails.category_id) ?: throw InvalidInputException("Can't find category_id!")

        var productId = Products.insert {
            it[Products.name] = productDetails.name
            it[Products.price] = productDetails.price
            it[Products.description] = productDetails.description
            it[Products.category] = category.id
        } get Products.id

        productDetails.copy(id = productId.value);
    }
}
