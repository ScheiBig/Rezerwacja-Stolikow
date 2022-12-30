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
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                name = obj.name
                mondayOpen = obj.openingHours.monday.from
                mondayClose = obj.openingHours.monday.to
                tuesdayOpen = obj.openingHours.tuesday.from
                tuesdayClose = obj.openingHours.tuesday.to
                wednesdayOpen = obj.openingHours.wednesday.from
                wednesdayClose = obj.openingHours.wednesday.to
                thursdayOpen = obj.openingHours.thursday.from
                thursdayClose = obj.openingHours.thursday.to
                fridayOpen = obj.openingHours.friday.from
                fridayClose = obj.openingHours.friday.to
                saturdayOpen = obj.openingHours.saturday.from
                saturdayClose = obj.openingHours.saturday.to
                sundayOpen = obj.openingHours.sunday.from
                sundayClose = obj.openingHours.sunday.to
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
        
        val tables by DiningTable.Entity referrersOn DiningTable.Table.restaurant
        
        fun toView() = View(
            this.name, Timetable.View(
                this.mondayOpen upTo this.mondayClose,
                this.tuesdayOpen upTo this.tuesdayClose,
                this.wednesdayOpen upTo this.wednesdayClose,
                thursdayOpen upTo thursdayClose,
                fridayOpen upTo fridayClose,
                saturdayOpen upTo saturdayClose,
                sundayOpen upTo sundayClose
            )
        )
    }
    
    @Serializable
    data class View(
        val name: String, val openingHours: Timetable.View
    )
}
