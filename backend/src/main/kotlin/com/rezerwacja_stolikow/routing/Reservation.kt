package com.rezerwacja_stolikow.routing

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

fun Routing.reservationRoutes() {
    route("dining_tables" / "reserve") {
        authenticate(Jwt.KEY) {
            put {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.LOCK) throw Jwt.AEType()
                val diningTable = Json.decodeFromString<DiningTable.SimpleView>(principal[Jwt.Claims.DINING_TABLE]!!)
                val bounds = Json.decodeFromString<DurationDate.AltView>(principal[Jwt.Claims.BOUNDS]!!)
                val personDetails = this.call.receiveOptional<Person.View>()
                    ?: throw IllegalArgumentException("Person details are missing")
                transaction {
                    val locks = PendingLock.Entity.find {
                        (PendingLock.Table.diningTableRestaurantID eq diningTable.restaurantID)
                            .and(PendingLock.Table.diningTableNumber eq diningTable.number)
                            .and(PendingLock.Table.startDateTime eq bounds.from.toEpochMilliseconds())
                            .and(PendingLock.Table.endDateTime eq bounds.toView().to.toEpochMilliseconds())
                    }
                    if (locks.empty()) throw PendingLock.NSEE(diningTable.restaurantID, diningTable.number, bounds)
                    locks.forEach(PendingLock.Entity::delete)
                    
                    Reservation.Entity
                        .fromView(Reservation.View(personDetails, diningTable, bounds))
                        .toView()
                        .copy(removalToken = Jwt.create(LocalDateTime.now + 2.minutes) {
                            withSubject(Jwt.Subjects.CANCEL)
                            withClaim(Jwt.Claims.DINING_TABLE, Json.encodeToString(diningTable))
                            withClaim(Jwt.Claims.BOUNDS, Json.encodeToString(bounds))
                            withClaim(Jwt.Claims.CLIENT, Json.encodeToString(personDetails))
                        })
                }.created respondTo this.call
            }
            
            delete {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.CANCEL) throw Jwt.AEType()
                val diningTable = Json.decodeFromString<DiningTable.SimpleView>(principal[Jwt.Claims.DINING_TABLE]!!)
                val bounds = Json.decodeFromString<DurationDate.AltView>(principal[Jwt.Claims.BOUNDS]!!)
                val personDetails = Json.decodeFromString<Person.View>(principal[Jwt.Claims.CLIENT]!!)
                transaction {
                    val reservations = Reservation.Entity.find {
                        (Reservation.Table.diningTableRestaurantID eq diningTable.restaurantID)
                            .and(Reservation.Table.diningTableNumber eq diningTable.number)
                            .and(Reservation.Table.startDateTime eq bounds.from.toEpochMilliseconds())
                            .and(Reservation.Table.endDateTime eq bounds.toView().to.toEpochMilliseconds())
                            .and(Reservation.Table.personFirstName eq personDetails.firstName)
                            .and(Reservation.Table.personLastName eq personDetails.lastName)
                            .and(Reservation.Table.personPhoneNumber eq personDetails.phoneNumber)
                    }
                    if (reservations.empty()) throw Reservation.NSEE(
                        diningTable.restaurantID, diningTable.number, bounds, personDetails
                    )
                    val count = reservations.count()
                    reservations.forEach(Reservation.Entity::delete)
                    if (count == 1L) "Cancelled 1 reservation" else "Cancelled $count reservations"
                }.accepted respondTo this.call
            }
        }
    }
}
