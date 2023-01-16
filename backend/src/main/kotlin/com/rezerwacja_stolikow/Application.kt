package com.rezerwacja_stolikow

import com.rezerwacja_stolikow.plugins.*
import com.rezerwacja_stolikow.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

// application.conf references the main function. This annotation prevents the IDE from marking it as unused.
@Suppress("unused")
fun Application.module() {
    
    configurePersistence()
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
