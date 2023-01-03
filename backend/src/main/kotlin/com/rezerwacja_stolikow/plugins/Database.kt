package com.rezerwacja_stolikow.plugins

import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.resource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {
    private lateinit var database: Database
    
    fun init(driverClass: String, databaseURL: String, username: String = "", password: String = "") {
        database = Database.connect(databaseURL, driverClass, username, password)
        transaction(database) {
            SchemaUtils.drop(Restaurant.Table, DiningTable.Table)
            SchemaUtils.create(Restaurant.Table, DiningTable.Table)
            
            val restaurantsFile = File(resource("data/Restaurants.json"))
            Json
                .decodeFromStream<List<Restaurant.View>>(restaurantsFile.inputStream())
                .forEach(Restaurant.Entity::fromView)
    
            val diningTablesFile = File(resource("DiningTables.json"))
            Json.decodeFromStream<List<DiningTable.View>>(diningTablesFile.inputStream())
                .forEach(DiningTable.Entity::fromView)
        }
    }
}
