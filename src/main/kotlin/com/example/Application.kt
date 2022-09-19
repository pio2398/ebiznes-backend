package com.example

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
    singleOf(::ProductServiceImpl) { bind<ProductService>() }
    singleOf(::DebugServiceImpl) { bind<DebugService>() }
    singleOf(::CategoriesServiceImpl) { bind<CategoriesService>() }
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
//    configureSecurity()
}
