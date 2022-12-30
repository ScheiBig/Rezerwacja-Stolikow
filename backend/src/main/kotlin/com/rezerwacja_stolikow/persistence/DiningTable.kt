package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object DiningTable {
    
    object Table: LongIdTable("dining_table") {
        val restaurant = reference("restaurant", Restaurant.Table)
        val number = integer("number")
        val smokingAllowed = bool("smoking_allowed")
        val byWindow = bool("by_window")
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                restaurant = Restaurant.Entity.findById(obj.restaurant)!!
                number = obj.number
                smokingAllowed = obj.smokingAllowed
                byWindow = obj.byWindow
            }
        }
        
        var restaurant by Restaurant.Entity referencedOn Table.restaurant
        var number by Table.number
        var smokingAllowed by Table.smokingAllowed
        var byWindow by Table.byWindow
        
        fun toView() = View(restaurant.id.value, number, smokingAllowed, byWindow)
    }
    
    @Serializable
    data class View(
        val restaurant: Long, val number: Int, val smokingAllowed: Boolean, val byWindow: Boolean
    )
}
