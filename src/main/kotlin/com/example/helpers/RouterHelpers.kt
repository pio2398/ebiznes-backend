package com.example.helpers

import com.example.exceptions.InvalidInputException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun ApplicationCall.getIdOfResource(name: String = "id"): Int {
    val param = parameters[name]
    return param?.toIntOrNull() ?: throw InvalidInputException("'${name}' must be int.")
}

suspend inline fun ApplicationCall.notFoundError() {
    request.call.respondText("404", status = HttpStatusCode.NotFound);
}

suspend inline fun <reified T : Any> ApplicationCall.respondOrNotFound(resource: T?) {
    if (resource == null) {
        request.call.notFoundError()
    } else {
        request.call.respond(resource)
    }
}