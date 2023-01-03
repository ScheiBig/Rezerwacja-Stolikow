package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Restaurant {
    
    object Table: LongIdTable("restaurants") {
        val name = varchar("name", 128)
        
        val mondayOpen = integer("monday_open")
        val mondayClose = integer("monday_close")
        val tuesdayOpen = integer("tuesday_open")
        val tuesdayClose = integer("tuesday_close")
        val wednesdayOpen = integer("wednesday_open")
        val wednesdayClose = integer("wednesday_close")
        val thursdayOpen = integer("thursday_open")
        val thursdayClose = integer("thursday_close")
        val fridayOpen = integer("friday_open")
        val fridayClose = integer("friday_close")
        val saturdayOpen = integer("saturday_open")
        val saturdayClose = integer("saturday_close")
        val sundayOpen = integer("sunday_open")
        val sundayClose = integer("sunday_close")
        
        val image = varchar("image", 256)
        val map = varchar("map", 256)
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                this.name = obj.name
                this.mondayOpen = obj.openingHours.monday.from
                this.mondayClose = obj.openingHours.monday.to
                this.tuesdayOpen = obj.openingHours.tuesday.from
                this.tuesdayClose = obj.openingHours.tuesday.to
                this.wednesdayOpen = obj.openingHours.wednesday.from
                this.wednesdayClose = obj.openingHours.wednesday.to
                this.thursdayOpen = obj.openingHours.thursday.from
                this.thursdayClose = obj.openingHours.thursday.to
                this.fridayOpen = obj.openingHours.friday.from
                this.fridayClose = obj.openingHours.friday.to
                this.saturdayOpen = obj.openingHours.saturday.from
                this.saturdayClose = obj.openingHours.saturday.to
                this.sundayOpen = obj.openingHours.sunday.from
                this.sundayClose = obj.openingHours.sunday.to
                this.image = obj.image
                this.map = obj.map
            }
        }
        
        var name by Table.name
        
        var mondayOpen by Table.mondayOpen
        var mondayClose by Table.mondayClose
        var tuesdayOpen by Table.tuesdayOpen
        var tuesdayClose by Table.tuesdayClose
        var wednesdayOpen by Table.wednesdayOpen
        var wednesdayClose by Table.wednesdayClose
        var thursdayOpen by Table.thursdayOpen
        var thursdayClose by Table.thursdayClose
        var fridayOpen by Table.fridayOpen
        var fridayClose by Table.fridayClose
        var saturdayOpen by Table.saturdayOpen
        var saturdayClose by Table.saturdayClose
        var sundayOpen by Table.sundayOpen
        var sundayClose by Table.sundayClose
        
        var image by Table.image
        var map by Table.map
        // unused
        // val tables by DiningTable.Entity referrersOn DiningTable.Table.restaurant
        
        fun toView() = View(
            this.id.value,
            this.name,
            Timetable.View(
                this.mondayOpen upTo this.mondayClose,
                this.tuesdayOpen upTo this.tuesdayClose,
                this.wednesdayOpen upTo this.wednesdayClose,
                this.thursdayOpen upTo this.thursdayClose,
                this.fridayOpen upTo this.fridayClose,
                this.saturdayOpen upTo this.saturdayClose,
                this.sundayOpen upTo this.sundayClose
            ),
            this.image,
            this.map
        )
    }
    
    @Serializable
    data class View(
        val ID: Long? = null, val name: String, val openingHours: Timetable.View, val image: String, val map: String
    )
}
