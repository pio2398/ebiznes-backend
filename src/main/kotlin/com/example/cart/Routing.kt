package com.example.cart;

import com.example.cart.dto.CartItemInput
import com.example.helpers.getIdOfResource
import com.example.helpers.getUserId
import com.example.services.CartService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.cartRoutes() {
    val cartService: CartService by inject()

    routing {
        authenticate("auth-jwt") {
            post("/cart") {
                val cartInput = call.receive<CartItemInput>()
                cartService.addProductToCart(call.getUserId(), cartInput.product_id)
            }

            delete("/cart/{id}") {
                cartService.removeProductFromCart(call.getUserId(), call.getIdOfResource())
            }
            get("/cart") {
                call.respond(HttpStatusCode.OK, cartService.getCart(call.getUserId()))
            }
        }
    }
}
