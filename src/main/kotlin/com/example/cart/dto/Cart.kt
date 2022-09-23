package com.example.cart.dto

import com.example.products.dto.ProductDetails
import kotlinx.serialization.Serializable

@Serializable
data class CartContent(
    val id: Int,
    val product: ProductDetails,
    val quantity: Int,
    val total_cost: Int,
)

@Serializable
data class Cart(
    val id: Int,
    val total_cost: Int,
    val contents: List<CartContent>
)

@Serializable
data class CartItemInput(
    val product_id: Int,
    val quantity: Int = 1
)