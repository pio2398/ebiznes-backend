package com.example.services

import com.example.helpers.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntent(
    val id: String,
    val amount: Int,
    val client_secret: String,
    val currency: String,
    val status: String
)

interface StripeService {
    suspend fun createPaymentIntent(amount: Int, currency: String = "PLN"): PaymentIntent;
    suspend fun retrievePaymentIntent(id: String): PaymentIntent;
    suspend fun cancelPaymentIntent(id: String): PaymentIntent;
}

class StripeServiceImpl(private val settingsService: SettingsService) : StripeService {
    override suspend fun createPaymentIntent(amount: Int, currency: String): PaymentIntent {
        val response: HttpResponse = httpClient.submitForm(
            url = "https://api.stripe.com/v1/payment_intents",
            formParameters = Parameters.build {
                append("amount", amount.toString())
                append("currency", currency)
                append("payment_method_types[]", "card")
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${settingsService.strip.secretKey}")
            }
        }
        return response.body()
    }

    override suspend fun retrievePaymentIntent(id: String): PaymentIntent {
        val response: HttpResponse = httpClient.get("https://api.stripe.com/v1/payment_intents/${id}") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${settingsService.strip.secretKey}")
            }
        }
        return response.body()
    }

    override suspend fun cancelPaymentIntent(id: String): PaymentIntent {
        val response: HttpResponse = httpClient.post("https://api.stripe.com/v1/payment_intents/${id}/cancel") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${settingsService.strip.secretKey}")
            }
        }
        return response.body()
    }
}