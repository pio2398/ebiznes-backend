package com.example.orders

import com.example.helpers.getUserId
import com.example.services.OrderService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.orderRoutes() {
    val orderService: OrderService by inject()

    routing {
        authenticate("auth-jwt") {

            get("/orders/my") {
                call.respond(orderService.getOrderOfUser(call.getUserId()))
            }

            get("/orders/cart") {
                call.respond(orderService.createOrderFromCart(call.getUserId()))
            }

        }
    }
}
