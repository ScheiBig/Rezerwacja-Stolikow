package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.serialization.PhoneNumberSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object Reservation {
    
    @Serializable
    data class View(
        val personDetails: Person.View,
        val diningTable: DiningTable.SimpleViewModeling,
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val removalToken: String? = null
    )
}

object Person {
    @Serializable
    data class View(
        val firstName: String,
        val lastName: String,
        @Serializable(with = PhoneNumberSerializer::class) val phoneNumber: String
    )
}

object DateTime {
    @Deprecated("Removing custom format", replaceWith = ReplaceWith("LocalDateTime", "kotlinx.datetime.LocalDateTime"))
    @Serializable
    data class View(
        val date: String,
        val time: String
    )
}
