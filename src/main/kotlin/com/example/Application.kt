package com.example

import com.example.plugins.configureAuth
import com.example.plugins.configureCors
import com.example.plugins.configureRouting
import com.example.services.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val koinModule = module {
    singleOf(::SettingsServiceImpl) { bind<SettingsService>() }
    singleOf(::ProductServiceImpl) { bind<ProductService>() }
    singleOf(::CategoriesServiceImpl) { bind<CategoriesService>() }
    singleOf(::UserServiceImpl) { bind<UserService>() }
    singleOf(::CartServiceImpl) { bind<CartService>() }
    singleOf(::OrderServiceImpl) { bind<OrderService>() }
    singleOf(::StripeServiceImpl) { bind<StripeService>() }
}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        modules(koinModule, koinDbModule, module { single<SettingsService> { SettingsServiceImpl(environment) } })
    }
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
    configureAuth()
    configureCors()
}
