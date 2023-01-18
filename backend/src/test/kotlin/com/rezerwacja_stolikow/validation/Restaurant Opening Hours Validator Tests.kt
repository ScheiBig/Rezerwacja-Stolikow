@file:Suppress("ClassName")

package com.rezerwacja_stolikow.validation

import com.rezerwacja_stolikow.persistence.Timetable
import com.rezerwacja_stolikow.persistence.upTo
import com.rezerwacja_stolikow.persistence.within
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

internal class `Restaurant Opening Hours Validator Tests` {
    companion object {
        val openingHours = Timetable.View(
            LocalTime.parse("07:00") upTo LocalTime.parse("21:00"),
            LocalTime.parse("08:00") upTo LocalTime.parse("20:00"),
            LocalTime.parse("12:00") upTo LocalTime.parse("22:00"),
            LocalTime.parse("10:00") upTo LocalTime.parse("00:00"),
            LocalTime.parse("12:00") upTo LocalTime.parse("01:00"),
            LocalTime.parse("14:00") upTo LocalTime.parse("05:00"),
            LocalTime.parse("05:00") upTo LocalTime.parse("18:00")
        )
    }
    
    @Test
    fun `test reservations after midnight`() {
        assertTrue {
            RestaurantOpeningHours
                .withOpeningHours(openingHours)
                .isWithin(LocalDateTime.parse("2023-01-22T02:00") within 2.hours)
        }
    }
    
    @Test
    fun `test overlapping opening time`() {
        assertFalse {
            RestaurantOpeningHours
                .withOpeningHours(openingHours)
                .isWithin(LocalDateTime.parse("2023-01-22T04:00") within 2.hours)
        }
    }
    
    @Test
    fun `test reservation before closing`() {
        assertTrue {
            RestaurantOpeningHours
                .withOpeningHours(openingHours)
                .isWithin(LocalDateTime.parse("2023-01-18T20:00") within 2.hours)
        }
    }
    
    @Test
    fun `test reservation after opening`() {
        assertTrue {
            RestaurantOpeningHours
                .withOpeningHours(openingHours)
                .isWithin(LocalDateTime.parse("2023-01-18T12:00") within 2.hours)
        }
    }
}
