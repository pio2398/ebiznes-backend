package com.example.helpers

import com.example.exceptions.InvalidInputException
import com.example.models.User
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun ApplicationCall.getIdOfResource(name: String = "id"): Int {
    val param = parameters[name]
    return param?.toIntOrNull() ?: throw InvalidInputException("'${name}' must be int.")
}

fun ApplicationCall.getStringOfResource(name: String = "id"): String? {
    return parameters[name];
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

fun ApplicationCall.getUser(): User? {
    val userService: UserService by inject()

    val p = principal<JWTPrincipal>()
    val payload = p?.payload
    return if (payload == null) {
        null;
    } else {
        val id = payload.getClaim("id").asInt();
        userService.userById(id)
    }
}