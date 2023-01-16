package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.serialization.PhoneNumberSerializer
import kotlinx.serialization.Serializable

object Person {
    @Serializable
    data class View(
        val firstName: String,
        val lastName: String,
        @Serializable(with = PhoneNumberSerializer::class) val phoneNumber: String
    )
}
