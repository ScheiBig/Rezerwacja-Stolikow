package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.serialization.PhoneNumberSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.hours

@Serializable
data class DiningTableQuery(
    val byWindow: Boolean? = null,
    val outside: Boolean? = null,
    val smokingAllowed: Boolean? = null
)

@Serializable
data class PhoneNumber(
    @Serializable(with = PhoneNumberSerializer::class) val phoneNumber: String
)


const val RESTAURANT_ID = "restaurantID"
const val ID = "ID"
val FULL_HOUR_MS = 1.hours.inWholeMilliseconds

const val RESTAURANTS = "restaurants"
const val RESERVATIONS = "reservations"
const val DINING_TABLES = "dining_tables"
const val SMS_CHECKING = "sms_checking"
const val LOCKS = "locks"
const val IMAGE = "image"
const val SEARCH = "search"
