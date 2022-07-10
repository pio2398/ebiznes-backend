package com.example.products.dto

import com.example.models.Product

@kotlinx.serialization.Serializable
data class ProductOnList(val name: String, val price: Double) {
    constructor(product: Product) : this(product.name, product.price)
}
