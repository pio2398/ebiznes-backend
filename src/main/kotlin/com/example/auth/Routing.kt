package com.example.auth

import com.example.helpers.getStringOfResource
import com.example.helpers.notFoundError
import com.example.helpers.respondOrNotFound
import com.example.services.AuthSession
import com.example.services.UserResponse
import com.example.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


private suspend fun ApplicationCall.respondUser(user: UserResponse) {
    val userService: UserService by inject()

    val token: String = userService.createToken(user)
    val oneTimeToken = userService.createOneTimeToken(AuthSession(token, user))
    respondRedirect("http://localhost:3001/handle_login/${oneTimeToken}")
}

fun Route.authRouting() {
    val userService: UserService by inject()

    get("/token/{id}") {
        val oneTimeToken = call.getStringOfResource();
        if (oneTimeToken == null) {
            call.notFoundError()
        } else {
            call.respondOrNotFound(userService.getByOneTimeToken(oneTimeToken))
        }
    }

    route("/google") {
        authenticate("auth-oauth-google") {
            get("/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                val accessToken = principal?.accessToken.toString()
                val user: UserResponse = userService.loginGoogle(accessToken)

                call.respondUser(user)
            }
        }
    }
    route("/github") {
        authenticate("github-oauth") {
            get("/login") {
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                val accessToken = principal?.accessToken.toString()
                val user: UserResponse = userService.loginGithub(accessToken)
                call.respondUser(user)
            }
        }
    }

    route("facebook") {
        authenticate("facebook-oauth") {
            get("/login") {
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                val accessToken = principal?.accessToken.toString()
                val user: UserResponse = userService.loginFacebook(accessToken)
                call.respondUser(user)
            }
        }
    }
}