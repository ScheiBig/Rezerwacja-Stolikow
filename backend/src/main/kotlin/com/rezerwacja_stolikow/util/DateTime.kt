package com.rezerwacja_stolikow.util

import kotlinx.datetime.*

fun LocalDateTime.Companion.fromEpochMilliseconds(epochMilliseconds: Long) = Instant
    .fromEpochMilliseconds(epochMilliseconds)
    .toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.toEpochMilliseconds() = this
    .toInstant(TimeZone.currentSystemDefault())
    .toEpochMilliseconds()
