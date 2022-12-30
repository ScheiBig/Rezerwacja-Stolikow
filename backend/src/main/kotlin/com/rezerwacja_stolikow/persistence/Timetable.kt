package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable

object Timetable {
    @Serializable
    data class View(
        val monday: OpeningHours.View,
        val tuesday: OpeningHours.View,
        val wednesday: OpeningHours.View,
        val thursday: OpeningHours.View,
        val friday: OpeningHours.View,
        val saturday: OpeningHours.View,
        val sunday: OpeningHours.View
    )
}
