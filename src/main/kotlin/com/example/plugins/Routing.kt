package com.example.plugins


import com.example.auth.authRouting
import com.example.cart.cartRoutes
import com.example.categories.categoriesRoutes
import com.example.products.productRoutes
import com.example.services.SettingsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val settingsService: SettingsService by inject()

    routing {
        get("/config") {
            call.respond(HttpStatusCode.OK)
        }
        route("/auth") {
            authRouting()
        }
    }

    productRoutes()
    categoriesRoutes()


    cartRoutes()

}
