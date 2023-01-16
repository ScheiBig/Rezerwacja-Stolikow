@file:OptIn(ExperimentalSerializationApi::class)

package com.rezerwacja_stolikow.plugins

import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.PendingLock
import com.rezerwacja_stolikow.persistence.Reservation
import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.resource
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {
    private lateinit var database: Database
    
    fun init(
        driverClass: String,
        databaseURL: String,
        username: String = "",
        password: String = "",
        shouldPurge: Boolean
    ) {
        database = Database.connect(databaseURL, driverClass, username, password)
        transaction(database) {
            SchemaUtils.drop(Restaurant.Table, DiningTable.Table)
            if (shouldPurge) {
                SchemaUtils.drop(PendingLock.Table, Reservation.Table)
            }
            SchemaUtils.create(Restaurant.Table, DiningTable.Table, PendingLock.Table, Reservation.Table)
            
            val restaurantsFile = File(resource("data/Restaurants.json"))
            Json
                .decodeFromStream<List<Restaurant.View>>(restaurantsFile.inputStream())
                .forEach(Restaurant.Entity::fromView)
            
            val diningTablesDirectory = File(resource("data/dining_tables"))
            diningTablesDirectory
                .listFiles { _, fileName ->
                    fileName
                        .split(".")
                        .getOrNull(1)
                        ?.let { it == "json" } ?: false
                }
                ?.forEach { jsonFile ->
                    Json
                        .decodeFromStream<List<DiningTable.View>>(jsonFile.inputStream())
                        .forEach(DiningTable.Entity::fromView)
                }
        }
    }
}

fun Application.configurePersistence() {
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
    val shouldPurge = environment.config
        .propertyOrNull("shouldPurge")
        ?.getString()
        .toBoolean()
    DatabaseFactory.init(driverClass, databaseURL, username, password, shouldPurge)
}
