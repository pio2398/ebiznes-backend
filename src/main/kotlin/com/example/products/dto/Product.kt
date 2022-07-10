package com.example.products.dto

import com.example.models.Product

@kotlinx.serialization.Serializable
data class ProductDetails(val name: String, val price: Double, val description: String?) {
    constructor(product: Product) : this(product.name, product.price, product.description)
}
