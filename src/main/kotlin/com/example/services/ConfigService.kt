package com.example.services

import kotlinx.serialization.Serializable

interface ConfigService {
    fun config(): ConfigResponse;
}


@Serializable
data class OAuthProviderResponse(
    val name: String,
    val url: String
)


@Serializable
data class ConfigResponse(
    val stripe_publishable_key: String?,
    val oauth_providers: List<OAuthProviderResponse>
)

class ConfigServiceImpl(private val settingsService: SettingsService) : ConfigService {
    override fun config(): ConfigResponse {
        return ConfigResponse(
            stripe_publishable_key = settingsService.strip.publishableKey,
            oauth_providers = listOf(
                OAuthProviderResponse(
                    name = "OAuth2",
                    url = "/com/example/auth/login",
                )
            ),
        )
    }
}