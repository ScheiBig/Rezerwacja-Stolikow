package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable

object Reservation {
    
    @Serializable
    data class View(
        val personDetails: Person.View,
        val dateTime: DateTime.View,
        val diningTable: DiningTable.SimpleView
    )
}

object Person {
    @Serializable
    data class View(
        val firstName: String, val lastName: String, val phoneNumber: String
    )
}

object DateTime {
    @Serializable
    data class View(
        val date: String,
        val time: String
    )
    
}
