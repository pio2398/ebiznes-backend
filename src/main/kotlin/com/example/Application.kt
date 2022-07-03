package com.example

import com.example.dao.DatabaseFactory
import com.example.dao.product.ProductDAO
import com.example.dao.product.ProductDAOImpl
import com.example.plugins.*
import com.example.services.ProductService
import com.example.services.ProductServiceImpl
import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.koin

val koinModule = module {
    singleOf(::ProductServiceImpl) { bind<ProductService>() }
    singleOf(::ProductDAOImpl) { bind<ProductDAO>() }
}

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    koin {
        modules(koinModule)
    }
    DatabaseFactory.init()
    configureRouting()
    configureSecurity()
}
