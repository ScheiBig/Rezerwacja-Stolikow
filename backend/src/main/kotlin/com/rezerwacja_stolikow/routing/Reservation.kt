package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.DataSpoiledException
import com.rezerwacja_stolikow.errors.NSEE
import com.rezerwacja_stolikow.persistence.*
import com.rezerwacja_stolikow.plugins.Jwt
import com.rezerwacja_stolikow.util.*
import com.rezerwacja_stolikow.validation.PhoneNumber
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.YearMonth
import kotlin.time.Duration.Companion.hours


fun Routing.reservationRoutes() {
    route(DINING_TABLES / RESERVATIONS) {
        authenticate(Jwt.KEY) {
            put {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.LOCK) throw Jwt.AEType()
                val diningTable = Json.decodeFromString<DiningTable.SimpleView>(principal[Jwt.Claims.DINING_TABLE]!!)
                val bounds = Json.decodeFromString<DurationDate.AltView>(principal[Jwt.Claims.BOUNDS]!!)
                if (bounds.from.toEpochMilliseconds() < LocalDateTime.now.toEpochMilliseconds()) {
                    throw DataSpoiledException("Reservation time has already passed")
                }
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
                        .copy(removalToken = Jwt.create {
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
            
            get {
                val principal = this.call.principal<JWTPrincipal>() ?: throw Jwt.AENone()
                if (principal.subject != Jwt.Subjects.ACCESS) throw Jwt.AEType()
                val phoneNumber = principal[Jwt.Claims.PHONE_NUMBER]!!.toLong()
                transaction {
                    Reservation.Entity
                        .find { Reservation.Table.personPhoneNumber eq PhoneNumber.toString(phoneNumber) }
                        .map(Reservation.Entity::toView)
                        .map {
                            it.copy(removalToken = Jwt.create {
                                withSubject(Jwt.Subjects.CANCEL)
                                withClaim(Jwt.Claims.DINING_TABLE, Json.encodeToString(it.diningTable))
                                withClaim(Jwt.Claims.BOUNDS, Json.encodeToString(it.bounds))
                                withClaim(Jwt.Claims.CLIENT, Json.encodeToString(it.personDetails))
                            })
                        }
                }.ok respondTo this.call
            }
        }
    }
    
    route(RESTAURANTS / "{$RESTAURANT_ID}" / RESERVATIONS) {
        post {
            val restaurantID = this.call
                .parameters(RESTAURANT_ID)
                .toLong()
            val q = this.call.receiveOptional<ReservationQuery>()
                ?: throw IllegalArgumentException("Query details are missing")
            
            if (transaction { Restaurant.Entity.findById(restaurantID) } == null) {
                throw Restaurant.NSEE(restaurantID)
            }
            
            val tableNumber = transaction {
                DiningTable.Entity
                    .find(DiningTable.Table.restaurant eq restaurantID)
                    .count()
            }
            
            val responseArray = Array(
                YearMonth
                    .of(q.date.year, q.date.month)
                    .lengthOfMonth()
            ) { 0L to tableNumber * 24 }
            
            transaction {
                Reservation.Entity
                    .find {
                        (Reservation.Table.diningTableRestaurantID eq restaurantID)
                            .and(
                                Reservation.Table.startDateTime greater LocalDateTime(
                                    q.date.year, q.date.month, 1, 0, 0
                                ).toEpochMilliseconds()
                            )
                            .and(
                                Reservation.Table.startDateTime less LocalDateTime(
                                    q.date.year,
                                    q.date.month,
                                    YearMonth
                                        .of(q.date.year, q.date.month)
                                        .lengthOfMonth(),
                                    23,
                                    59
                                ).toEpochMilliseconds()
                            )
                    }
                    .map(Reservation.Entity::toView)
                    .forEach { r ->
                        responseArray[r.bounds.from.dayOfMonth] = responseArray[r.bounds.from.dayOfMonth].let {
                            it.copy(first = it.first + r.bounds.durationH)
                        }
                    }
            }
            
            responseArray
                .map { (n, d) -> n.toFloat() / d }
                .drop(1).ok respondTo this.call
        }
        
        post(SEARCH) {
            val restaurantID = this.call
                .parameters(RESTAURANT_ID)
                .toLong()
            val q = this.call.receiveOptional<ReservationQuery>()
                ?: throw IllegalArgumentException("Query details are missing")
            if (q.durationH == null) throw IllegalArgumentException("Query details are missing")
            
            val bounds = q.date within q.durationH.hours
            
            if (transaction { Restaurant.Entity.findById(restaurantID) } == null) {
                throw Restaurant.NSEE(restaurantID)
            }
            
            transaction {
                var tablesE = DiningTable.Table.restaurant eq restaurantID
                if (q.filter != null) {
                    q.filter.byWindow?.let { tablesE = tablesE.and { DiningTable.Table.byWindow eq q.filter.byWindow } }
                    q.filter.outside?.let { tablesE = tablesE.and { DiningTable.Table.outside eq q.filter.outside } }
                    q.filter.smokingAllowed?.let {
                        tablesE = tablesE.and { DiningTable.Table.smokingAllowed eq q.filter.smokingAllowed }
                    }
                }
                DiningTable.Entity
                    .find(tablesE)
                    .filter {
                        Reservation.Entity
                            .findConflictingReservations(it, bounds)
                            .empty() && PendingLock.Entity
                            .findConflictingLocks(it, bounds)
                            .empty()
                    }
                    .map(DiningTable.Entity::toSimpleView)
            }.ok respondTo this.call
        }
    }
}
