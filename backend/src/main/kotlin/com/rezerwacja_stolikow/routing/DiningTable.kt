package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.invoke
import com.rezerwacja_stolikow.util.ok
import com.rezerwacja_stolikow.util.respondTo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.diningTableRoutes() {
    route("dining_tables") {
        get("search") {
            val param = this.call.parameters
            val restaurantId = param("restaurantId").toLong()
            val byWindow = param["byWindow"]?.toBooleanStrict()
            val outside = param["outside"]?.toBooleanStrict()
            val smokingAllowed = param["smokingAllowed"]?.toBooleanStrict()
            
            if (transaction { Restaurant.Entity.findById(restaurantId) } == null) {
                throw NoSuchElementException("No such restaurant: $restaurantId")
            }
            
            transaction {
                val tablesQ = DiningTable.Table.select(DiningTable.Table.restaurant eq restaurantId)
                byWindow?.let { tablesQ.andWhere { DiningTable.Table.byWindow eq byWindow } }
                outside?.let { tablesQ.andWhere { DiningTable.Table.outside eq outside } }
                smokingAllowed?.let { tablesQ.andWhere { DiningTable.Table.smokingAllowed eq smokingAllowed } }
                DiningTable.Entity
                    .wrapRows(tablesQ)
                    .map(DiningTable.Entity::toView)
            }.ok respondTo this.call
        }
    }
}
