package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable

object OpeningHours {
    @Serializable
    data class View(
        val from: Int, val to: Int
    )
}

infix fun Int.upTo(to: Int) = OpeningHours.View(this, to)
