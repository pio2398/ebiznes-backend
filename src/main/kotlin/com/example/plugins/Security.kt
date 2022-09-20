package com.example.plugins


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.exceptions.PermissionDeniedException
import com.example.helpers.httpClient
import com.example.services.SettingsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuth() {
    val settingsService: SettingsService by inject()

    install(Authentication) {
        jwt("auth-jwt") {
            //realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(settingsService.jwt.jwtSecret))
                    //.withAudience(audience)
                    .withIssuer(settingsService.jwt.jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("nickname").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _defaultScheme, _realm ->
                throw PermissionDeniedException("Token is not valid or has expired")
            }
        }
        oauth("auth-oauth-google") {
            urlProvider = { "${settingsService.projectDomain.domain}/auth/google/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = settingsService.oAuthGoogle.clientId,
                    clientSecret = settingsService.oAuthGoogle.clientSecret,
                    defaultScopes = listOf(
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/userinfo.email"
                    )
                )
            }
            client = httpClient
        }
        oauth("github-oauth") {
            urlProvider = { "${settingsService.projectDomain}/auth/github/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    accessTokenUrl = "https://github.com/login/oauth/access_token",
                    authorizeUrl = "https://github.com/login/oauth/authorize",
                    requestMethod = HttpMethod.Post,
                    clientId = settingsService.oAuthGoogle.clientId,
                    clientSecret = settingsService.oAuthGoogle.clientSecret,
                    defaultScopes = listOf(
                        "user:email", "read:user"
                    )
                )
            }
            client = httpClient
        }

        oauth("facebook-oauth") {
            urlProvider = { "${settingsService.projectDomain}/auth/facebook/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    accessTokenUrl = "https://graph.facebook.com/v14.0/oauth/access_token",
                    authorizeUrl = "https://www.facebook.com/v14.0/dialog/oauth",
                    requestMethod = HttpMethod.Get,
                    clientId = settingsService.oAuthGoogle.clientId,
                    clientSecret = settingsService.oAuthGoogle.clientSecret,
                    defaultScopes = listOf(
                        "public_profile", "email"
                    )
                )
            }
            client = httpClient
        }
    }
}