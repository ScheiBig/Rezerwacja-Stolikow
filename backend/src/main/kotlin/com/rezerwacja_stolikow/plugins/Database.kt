@file:OptIn(ExperimentalSerializationApi::class)

package com.rezerwacja_stolikow.plugins

import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.PendingLock
import com.rezerwacja_stolikow.persistence.Reservation
import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.PrettyJson
import com.rezerwacja_stolikow.util.resource
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
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
            if (shouldPurge) {
                SchemaUtils.drop(Reservation.Table)
                SchemaUtils.drop(PendingLock.Table)
                SchemaUtils.drop(DiningTable.Table)
                SchemaUtils.drop(Restaurant.Table)
                
            }
            SchemaUtils.create(Restaurant.Table, DiningTable.Table, PendingLock.Table, Reservation.Table)
            LinkedHashMap<String, Int>().toString()
            if (shouldPurge) {
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
    
    fun printData() = listOfNotNull(transaction {
        PendingLock.Entity
            .all()
            .let { locks ->
                if (locks.empty()) null else {
                    (listOf("MainDatabase::[[ PendingLocks: ]]") + locks
                        .map(PendingLock.Entity::toView)
                        .map(PrettyJson::encodeToString)
                        .mapIndexed { i, v -> "$i: $v" }).joinToString("\n")
                }
            }
    }, transaction {
        Reservation.Entity
            .all()
            .let { locks ->
                if (locks.empty()) null else {
                    (listOf("MainDatabase::[[ Reservations: ]]") + locks
                        .map(Reservation.Entity::toView)
                        .map(PrettyJson::encodeToString)
                        .mapIndexed { i, v -> "$i: $v" }).joinToString("\n")
                }
            }
    })
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
    if (shouldPurge) this.environment.log.warn("Database purge issued! All data is going to be lost")
    DatabaseFactory.init(driverClass, databaseURL, username, password, shouldPurge)
    if (!shouldPurge) {
        val data = DatabaseFactory.printData()
        this.environment.log.info("Database not purged! Current data:")
        data.forEach(this.environment.log::info)
    }
}
