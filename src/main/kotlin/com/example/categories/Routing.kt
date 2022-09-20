package com.example.categories

import com.example.categories.dto.CategoryDTO
import com.example.exceptions.InvalidInputException
import com.example.helpers.getIdOfResource
import com.example.helpers.respondOrNotFound
import com.example.services.CategoriesService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Application.categoriesRoutes() {
    val categoryService: CategoriesService by inject()

    routing {
        get("/categories") {
            call.respond(categoryService.getCategoriesList())
        }
        get("/categories/main_page") {
            call.respond(categoryService.getCategoriesListOnMainPage())
        }
        post("/categories") {
            try {
                val category = call.receive<CategoryDTO>()
                call.respond(categoryService.addCategory(category))
            } catch (e: SerializationException) {
                call.respondText("{'error' : '${e.message}'", status = HttpStatusCode.BadRequest)
            } catch (e: InvalidInputException) {
                call.respondText("{'error' : '${e.message}'", status = HttpStatusCode.BadRequest)
            }
        }
        get("/categories/{id}") {
            call.respondOrNotFound(categoryService.getCategory(call.getIdOfResource()));
        }
    }
}
