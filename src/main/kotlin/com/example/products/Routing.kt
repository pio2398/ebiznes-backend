package com.example.products

import com.example.exceptions.InvalidInputException
import com.example.helpers.getIdOfResource
import com.example.helpers.respondOrNotFound
import com.example.products.dto.ProductDetails
import com.example.services.ProductService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Application.productRoutes() {
    val productService: ProductService by inject()

    routing {
        get("/products") {
            call.respond(productService.getProductsList())
        }
        post("/products") {
            try {
                val product = call.receive<ProductDetails>()
                call.respond(productService.addProduct(product))
            } catch (e: SerializationException) {
                call.respondText("{'error' : '${e.message}'", status = HttpStatusCode.BadRequest)
            } catch (e: InvalidInputException) {
                call.respondText("{'error' : '${e.message}'", status = HttpStatusCode.BadRequest)
            }
        }
        get("/products/{id}") {
            call.respondOrNotFound(productService.getProduct(call.getIdOfResource()));
        }
    }
}
