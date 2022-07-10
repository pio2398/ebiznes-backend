package com.example.products

import com.example.services.ProductService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.productRoutes() {
    val productService: ProductService by inject()

    routing {
        get("/products") {
            call.respond(productService.getProductsList())
        }
    }
}
