package com.rezerwacja_stolikow

import io.ktor.server.application.*
import com.rezerwacja_stolikow.plugins.*
import com.rezerwacja_stolikow.routing.configureRouting
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val driverClass = environment.config
        .property("database.driver")
        .getString()
    val databaseURL = environment.config
        .property("database.url")
        .getString()
    val username = environment.config
        .propertyOrNull("database.username")
        ?.getString() ?: ""
    val password = environment.config
        .propertyOrNull("database.password")
        ?.getString() ?: ""
    DatabaseFactory.init(driverClass, databaseURL, username, password)
    
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
