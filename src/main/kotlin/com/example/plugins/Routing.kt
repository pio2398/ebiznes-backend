package com.example.plugins

import com.example.products.productRoutes
import com.example.services.DebugService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val debugService: DebugService by inject()

    routing {
        get("/debug") {
            call.respondText(debugService.debug())
        }
    }
    productRoutes()
}
