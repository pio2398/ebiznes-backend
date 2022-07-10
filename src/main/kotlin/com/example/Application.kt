package com.example

import com.example.plugins.*
import com.example.services.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.ext.inject

val koinModule = module {
    singleOf(::ProductServiceImpl) { bind<ProductService>() }
    singleOf(::DebugServiceImpl) { bind<DebugService>() }
}

fun main(args: Array<String>) {
    GlobalContext.startKoin {}
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    loadKoinModules(koinModule)
    loadKoinModules(koinDbModule)
    val databaseFactory: DatabaseFactory by inject()

    databaseFactory.init()
    configureRouting()
    configureSecurity()
    install(ContentNegotiation) {
        json()
    }
}
