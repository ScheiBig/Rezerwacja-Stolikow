package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.util.minus
import com.rezerwacja_stolikow.util.plus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object DurationDate {
    @Serializable
    data class View(
        val from: LocalDateTime,
        val to: LocalDateTime
    ) {
        fun toAltView() = AltView(from, (to - from).inWholeHours)
    }
    
    @Serializable
    data class AltView(
        val from: LocalDateTime,
        val durationH: Long
    ) {
        init {
            if (durationH <= 0L) throw IllegalArgumentException("Duration must be positive")
        }
        
        fun toView() = View(from, from + durationH.hours)
    }
}

infix fun LocalDateTime.within(duration: Duration) = DurationDate.AltView(this, duration.inWholeHours)
