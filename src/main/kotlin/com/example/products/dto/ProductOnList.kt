package com.example.products.dto

import com.example.models.Product

@kotlinx.serialization.Serializable
data class ProductOnList(val id: Int, val name: String, val price: Int) {
    constructor(product: Product) : this(product.id.value, product.name, product.price)
}
