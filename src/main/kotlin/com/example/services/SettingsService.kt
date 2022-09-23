package com.example.services

import io.ktor.server.application.*

data class Jwt(val jwtSecret: String, val jwtIssuer: String)

data class ProjectsUrls(val backend: String, val frontend: String)

data class OAuth(val clientId: String, val clientSecret: String)

data class Strip(val publishableKey: String, val secretKey: String);

interface SettingsService {
    val projectUrls: ProjectsUrls;
    val jwt: Jwt;
    val oAuthGoogle: OAuth
    val oAuthGithub: OAuth
    val oAuthFacebook: OAuth
    val strip: Strip

}


class SettingsServiceImpl(private val environment: ApplicationEnvironment) : SettingsService {
    override val projectUrls: ProjectsUrls
        get() = ProjectsUrls(
            environment.config.propertyOrNull("settings.domain")!!.getString(),
            environment.config.propertyOrNull("settings.frontend")!!.getString()
        )

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

    override val strip: Strip
        get() = Strip(
            environment.config.propertyOrNull("stripe.publishableKey")!!.getString(),
            environment.config.propertyOrNull("stripe.secretKey")!!.getString()
        )

}

