package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.NSEE
import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.Restaurant
import com.rezerwacja_stolikow.util.invoke
import com.rezerwacja_stolikow.util.ok
import com.rezerwacja_stolikow.util.receiveOptional
import com.rezerwacja_stolikow.util.respondTo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class DiningTableQuery(
    val byWindow: Boolean? = null,
    val outside: Boolean? = null,
    val smokingAllowed: Boolean? = null
)

fun Routing.diningTableRoutes() {
    route("dining_tables") {
        get("search/{restaurantID}") {
            val restaurantID = this.call
                .parameters("restaurantId")
                .toLong()
            val q = this.call.receiveOptional<DiningTableQuery>()
            
            if (transaction { Restaurant.Entity.findById(restaurantID) } == null) {
                throw Restaurant.NSEE(restaurantID)
            }
            
            return@get if (q == null) {
                transaction {
                    DiningTable.Entity
                        .find(DiningTable.Table.restaurant eq restaurantID)
                        .map(DiningTable.Entity::toView)
                }.ok respondTo this.call
            } else {
                transaction {
                    var tablesE = DiningTable.Table.restaurant eq restaurantID
                    q.byWindow?.let { tablesE = tablesE.and { DiningTable.Table.byWindow eq q.byWindow } }
                    q.outside?.let { tablesE = tablesE.and { DiningTable.Table.outside eq q.outside } }
                    q.smokingAllowed?.let {
                        tablesE = tablesE.and { DiningTable.Table.smokingAllowed eq q.smokingAllowed }
                    }
                    DiningTable.Entity
                        .find(tablesE)
                        .map(DiningTable.Entity::toSimpleView)
                }.ok respondTo this.call
            }
            
        }
    }
}
