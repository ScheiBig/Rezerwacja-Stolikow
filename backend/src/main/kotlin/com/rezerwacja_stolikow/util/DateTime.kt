package com.rezerwacja_stolikow.util

import kotlinx.datetime.*
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun LocalDateTime.Companion.fromEpochMilliseconds(epochMilliseconds: Long) = Instant
    .fromEpochMilliseconds(epochMilliseconds)
    .toLocalDateTime(tz)

fun LocalDateTime.toEpochMilliseconds() = this
    .toInstant(tz)
    .toEpochMilliseconds()

val LocalDateTime.Companion.now
    get() = Clock.System
        .now()
        .toLocalDateTime(tz)

operator fun LocalDateTime.plus(duration: Duration) = this
    .toInstant(tz)
    .plus(duration)
    .toLocalDateTime(tz)

operator fun LocalDateTime.minus(duration: Duration) = this
    .toInstant(tz)
    .minus(duration)
    .toLocalDateTime(tz)

operator fun LocalDateTime.minus(until: LocalDateTime) = (this
    .toEpochMilliseconds() to until
    .toEpochMilliseconds())
    .let { (thisMs, untilMs) ->
        (max(thisMs, untilMs) to min(thisMs, untilMs)).let { (more, less) -> (more - less).milliseconds }
    }

private val tz = TimeZone.currentSystemDefault()
