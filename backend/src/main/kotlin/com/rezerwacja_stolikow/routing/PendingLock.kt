package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.AuthenticationException
import com.rezerwacja_stolikow.errors.AuthorizationException
import com.rezerwacja_stolikow.errors.DLE
import com.rezerwacja_stolikow.errors.NSEE
import com.rezerwacja_stolikow.persistence.*
import com.rezerwacja_stolikow.plugins.Jwt
import com.rezerwacja_stolikow.util.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val LOCK = "lock"
private const val DINING_TABLE = "diningTable"
private const val BOUNDS = "bounds"

fun Routing.pendingLockRoutes() {
    route("dining_tables" / "lock") {
        put {
            val lock = this.call.receiveOptional<PendingLock.View>()
                ?: throw IllegalArgumentException("Lock details are missing")
            val diningTable =
                transaction { DiningTable.Entity.findByForeign(lock.diningTable.restaurantID, lock.diningTable.number) }
                    ?: throw DiningTable.NSEE(lock.diningTable.restaurantID, lock.diningTable.number)
            
            val expiration = LocalDateTime.now + (2.minutes + 5.seconds)
            
            transaction {
                if (PendingLock.Entity
                        .findConflictingLocks(diningTable, lock.bounds)
                        .empty()
                        .not()
                ) {
                    throw PendingLock.DLE(diningTable.restaurant.id.value, diningTable.number, lock.bounds)
                }
                PendingLock.Entity.fromView(lock.copy(expirationDateTime = expiration))
            }
            Jwt.create(expiration) {
                withSubject(LOCK)
                    .withClaim(DINING_TABLE, Json.encodeToString(diningTable.toSimpleView()))
                    .withClaim(BOUNDS, Json.encodeToString(lock.bounds))
            }.ok respondTo this.call
        }
        
        authenticate(Jwt.key) {
            delete {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.LOCK) throw Jwt.AEType()
                val diningTable = Json.decodeFromString<DiningTable.SimpleView>(principal[DINING_TABLE]!!)
                val bounds = Json.decodeFromString<DurationDate.AltView>(principal[BOUNDS]!!)
                transaction {
                    val locks = PendingLock.Entity.find {
                        (PendingLock.Table.diningTableRestaurantID eq diningTable.restaurantID)
                            .and(PendingLock.Table.diningTableNumber eq diningTable.number)
                            .and(PendingLock.Table.startDateTime eq bounds.from.toEpochMilliseconds())
                            .and(PendingLock.Table.endDateTime eq bounds.toView().to.toEpochMilliseconds())
                    }
                    if (locks.empty()) throw PendingLock.NSEE(
                        diningTable.restaurantID, diningTable.number, bounds
                    )
                    val count = locks.count()
                    locks.forEach(PendingLock.Entity::delete)
                    "Unlocked $count reservations"
                }.accepted respondTo this.call
            }
        }
    }
    
}
