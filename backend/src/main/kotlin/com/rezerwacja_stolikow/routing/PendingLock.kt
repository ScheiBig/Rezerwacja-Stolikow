package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.DLE
import com.rezerwacja_stolikow.errors.DTE
import com.rezerwacja_stolikow.errors.DataSpoiledException
import com.rezerwacja_stolikow.errors.NSEE
import com.rezerwacja_stolikow.persistence.DiningTable
import com.rezerwacja_stolikow.persistence.DurationDate
import com.rezerwacja_stolikow.persistence.PendingLock
import com.rezerwacja_stolikow.persistence.Reservation
import com.rezerwacja_stolikow.plugins.Jwt
import com.rezerwacja_stolikow.util.*
import com.rezerwacja_stolikow.validation.RestaurantOpeningHours
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


fun Routing.pendingLockRoutes() {
    route(DINING_TABLES / LOCKS) {
        put {
            var lock = this.call.receiveOptional<PendingLock.View>()
                ?: throw IllegalArgumentException("Lock details are missing")
            if (lock.bounds.from.toEpochMilliseconds() % FULL_HOUR_MS != 0L) {
                lock = lock.copy(
                    bounds = lock.bounds.copy(
                        from = LocalDateTime.fromEpochMilliseconds(lock.bounds.from.toEpochMilliseconds() / FULL_HOUR_MS * FULL_HOUR_MS)
                    )
                )
            }
            if (lock.bounds.from.toEpochMilliseconds() < LocalDateTime.now.toEpochMilliseconds()) {
                throw DataSpoiledException("Reservation time has already passed")
            }
            val diningTable =
                transaction { DiningTable.Entity.findByForeign(lock.diningTable.restaurantID, lock.diningTable.number) }
                    ?: throw DiningTable.NSEE(lock.diningTable.restaurantID, lock.diningTable.number)
            
            val expiration = LocalDateTime.now + (2.minutes + 5.seconds)
            
            transaction {
                if (RestaurantOpeningHours
                        .withOpeningHours(diningTable.restaurant.toView().openingHours)
                        .isWithin(lock.bounds)
                        .not()
                ) throw NullPointerException("Reservation bounds outside of restaurant opening hours")
                
                if (PendingLock.Entity
                        .findConflictingLocks(diningTable, lock.bounds)
                        .empty()
                        .not()
                ) throw PendingLock.DLE(diningTable.restaurant.id.value, diningTable.number, lock.bounds)
                if (Reservation.Entity
                        .findConflictingReservations(diningTable, lock.bounds)
                        .empty()
                        .not()
                ) throw Reservation.DTE(diningTable.restaurant.id.value, diningTable.number, lock.bounds)
                PendingLock.Entity.fromView(lock.copy(expirationDateTime = expiration))
            }
            Jwt.create(expiration) {
                withSubject(Jwt.Subjects.LOCK)
                withClaim(Jwt.Claims.DINING_TABLE, Json.encodeToString(diningTable.toSimpleView()))
                withClaim(Jwt.Claims.BOUNDS, Json.encodeToString(lock.bounds))
            }.ok respondTo this.call
        }
        
        authenticate(Jwt.KEY) {
            delete {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.LOCK) throw Jwt.AEType()
                val diningTable = Json.decodeFromString<DiningTable.SimpleView>(principal[Jwt.Claims.DINING_TABLE]!!)
                val bounds = Json.decodeFromString<DurationDate.AltView>(principal[Jwt.Claims.BOUNDS]!!)
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
                    if (count == 1L) "Unlocked 1 reservation" else "Unlocked $count reservations"
                }.accepted respondTo this.call
            }
        }
    }
}
