package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.ok
import com.rezerwacja_stolikow.util.respondTo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.restaurantRoutes() {
    route("restaurants") {
        get {
            transaction {
                Restaurant.Entity
                    .all()
                    .map(Restaurant.Entity::toView)
            }.ok respondTo this.call
        }
    }
}
