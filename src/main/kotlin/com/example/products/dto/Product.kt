package com.example.products.dto

import com.example.models.Product

@kotlinx.serialization.Serializable
data class ProductDetails(
    val name: String,
    val price: Int,
    val category_id: Int,
    val product_id: Int? = null,
    val description: String? = null,
    val picture_url: String? = null
) {
    constructor(product: Product) : this(
        product.name,
        product.price,
        product.category.value,
        product.id.value,
        product.description,
        product.picture_url
    )

}
