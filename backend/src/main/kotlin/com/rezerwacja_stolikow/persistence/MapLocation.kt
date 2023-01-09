package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable

object MapLocation {
    
    @Serializable
    data class View(
        val x: Int,
        val y: Int,
        val w: Int,
        val h: Int
    )
}
