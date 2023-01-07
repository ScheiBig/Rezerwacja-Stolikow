package com.rezerwacja_stolikow.persistence

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

object OpeningHours {
    @Serializable
    data class View(
        val from: LocalTime,
        val to: LocalTime
    )
}

infix fun LocalTime.upTo(to: LocalTime) = OpeningHours.View(this, to)
