package com.example.services

import io.ktor.server.application.*

data class Jwt(val jwtSecret: String, val jwtIssuer: String)

data class Settings(val domain: String)

data class OAuth(val clientId: String, val clientSecret: String)

interface SettingsService {
    val projectDomain: Settings;
    val jwt: Jwt;
    val oAuthGoogle: OAuth
    val oAuthGithub: OAuth
    val oAuthFacebook: OAuth

}


class SettingsServiceImpl(private val environment: ApplicationEnvironment) : SettingsService {
    override val projectDomain: Settings
        get() = Settings(environment.config.propertyOrNull("settings.domain")!!.getString())

    override val jwt: Jwt
        get() = Jwt(
            jwtSecret = environment.config.propertyOrNull("jwt.secret")!!.getString(),
            jwtIssuer = environment.config.propertyOrNull("jwt.issuer")!!.getString()
        )
    override val oAuthGoogle: OAuth
        get() = OAuth(
            environment.config.propertyOrNull("oauth.google.clientId")!!.getString(),
            environment.config.propertyOrNull("oauth.google.clientSecret")!!.getString()
        )

    override val oAuthGithub: OAuth
        get() = OAuth(
            environment.config.propertyOrNull("oauth.github.clientId")!!.getString(),
            environment.config.propertyOrNull("oauth.github.clientSecret")!!.getString()
        )

    override val oAuthFacebook: OAuth
        get() = OAuth(
            environment.config.propertyOrNull("oauth.facebook.clientId")!!.getString(),
            environment.config.propertyOrNull("oauth.facebook.clientSecret")!!.getString()
        )
}

