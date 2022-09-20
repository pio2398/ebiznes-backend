package com.example.auth

import com.example.services.RedirectUrlSession
import com.example.services.UserResponse
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject


@Serializable
private data class LoginResponse(
    val user: UserResponse,
    val token: String,
)

private fun addTokenParamToUrl(baseUrl: String, token: String): String {
    val token_param = listOf(Pair("token", token)).formUrlEncode()

    val v1 = baseUrl.split("#")
    val delimeter = if (v1[0].contains("?")) "&" else "?"
    var res = v1[0] + delimeter + token_param
    return if (v1.size == 1) res else res + "#" + v1[1]
}

private suspend fun ApplicationCall.respondUser(user: UserResponse) {
    val userService: UserService by inject()

    val token: String = userService.createToken(user)
    val redirectUrlSession = sessions.get<RedirectUrlSession>()

    if (redirectUrlSession != null) {
        respondRedirect(addTokenParamToUrl(redirectUrlSession.url, token))
    } else {
        respond(LoginResponse(user, token))
    }
}

fun Route.authRouting() {
    val userService: UserService by inject()

    get("/login") {

        val redirectUrl = call.request.queryParameters["state_url"];
        println(redirectUrl)
        if (redirectUrl != null) {
            call.sessions.set(RedirectUrlSession(redirectUrl))
        } else {
            call.sessions.clear<RedirectUrlSession>();
        }
        call.respondRedirect("/login_screen.html")
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