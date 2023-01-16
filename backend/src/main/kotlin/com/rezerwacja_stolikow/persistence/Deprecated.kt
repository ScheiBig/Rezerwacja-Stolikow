package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable

@Deprecated("Removing custom format")
object DateTime {
    @Deprecated("Removing custom format", replaceWith = ReplaceWith("LocalDateTime", "kotlinx.datetime.LocalDateTime"))
    @Serializable
    data class View(
        val date: String,
        val time: String
    )
}
