@file:Suppress("FunctionName", "SpellCheckingInspection", "UnusedReceiverParameter")

package com.rezerwacja_stolikow.errors

import com.rezerwacja_stolikow.persistence.*
import io.ktor.http.*
import kotlin.time.Duration.Companion.seconds

@DslMarker
annotation class ThrowableFactory

@ThrowableFactory
fun Restaurant.NSEE(
    restaurantID: Long
) = NoSuchElementException("No such restaurant: $restaurantID")

@ThrowableFactory
fun DiningTable.NSEE(
    restaurantID: Long,
    number: Int
) = NoSuchElementException("No such dining table: $number in restaurant $restaurantID")

@Suppress("FunctionName")
@ThrowableFactory
fun PendingLock.DLE(
    restaurantID: Long,
    number: Int,
    bounds: DurationDate.AltView
) = DataLockedException(
    "Dining table already locked: $number in restaurant $restaurantID, ${
        bounds.durationS.seconds
    } from ${bounds.from}"
)

@ThrowableFactory
fun PendingLock.NSEE(
    restaurantID: Long,
    number: Int,
    bounds: DurationDate.AltView
) = NoSuchElementException(
    "No such lock on dining table: $number in restaurant $restaurantID, ${bounds.durationS.seconds} from ${bounds.from}"
)

@ThrowableFactory
fun Reservation.DTE(
    restaurantID: Long,
    number: Int,
    bounds: DurationDate.AltView
) = DataTakenException(
    "Dining table already reserved: $number in restaurant $restaurantID, ${
        bounds.durationS.seconds
    } from ${bounds.from}"
)

@ThrowableFactory
fun Reservation.NSEE(
    restaurantID: Long,
    number: Int,
    bounds: DurationDate.AltView,
    personDetails: Person.View
) = NoSuchElementException(
    "No such reservation for dining table: $number in restaurant $restaurantID, ${bounds.durationS.seconds} from ${
        bounds.from
    }, by ${personDetails.firstName} ${personDetails.lastName} (#${personDetails.phoneNumber})"
)

@ThrowableFactory
fun ContentType.Image.NSEE(
    ID: String
) = NoSuchElementException("No such image with id: $ID")
