package com.example.orders.dto

import com.example.models.Order

@kotlinx.serialization.Serializable
data class Order(
    val id: Int,
    val user_id: Int,
    val total_cost: Int,
    val paid: Boolean,
    val ts: String?,
    val payment_client_secret: String?
) {
    constructor(order: Order) : this(
        order.id.value,
        order.user.value,
        order.total_cost,
        order.paid,
        order.ts,
        order.payment_client_secret
    )

}