package com.rezerwacja_stolikow.plugins

import com.rezerwacja_stolikow.persistence.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import resource
import java.io.File

object DatabaseFactory {
    private lateinit var database: Database
    
    fun init(driverClass: String, databaseURL: String, username: String = "", password: String = "") {
        database = Database.connect(databaseURL, driverClass, username, password)
    }
}