package com.example.services

import com.example.exceptions.InvalidInputException
import com.example.models.Orders
import com.example.orders.dto.Order
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

interface OrderService {
    fun getOrderOfUser(user_id: Int?): List<Order>
    suspend fun createOrderFromCart(user_id: Int?): Order
}


class OrderServiceImpl(
    private val databaseFactory: DatabaseFactory,
    private val cartService: CartService,
    private val stripeService: StripeService
) : OrderService {

    override fun getOrderOfUser(user_id: Int?): List<Order> = transaction(databaseFactory.dataBase) {
        if (user_id == null) {
            throw InvalidInputException("Not valid user_id.")
        }
        return@transaction com.example.models.Order.find(Orders.user eq user_id).map { Order(it) }

    }

    override suspend fun createOrderFromCart(user_id: Int?): Order {
        if (user_id == null) {
            throw InvalidInputException("Not valid user_id.")
        }
        val cart = cartService.getCart(user_id)
        val secret = stripeService.createPaymentIntent(cart.total_cost)


        val orderId = Orders.insert {
            it[user] = user_id
            it[payment_client_secret] = secret.client_secret
            it[paid] = false
            it[total_cost] = cart.total_cost
        } get Orders.id

        return Order(orderId.value, user_id, cart.total_cost, false, null, secret.client_secret)
    }


}