package com.rezerwacja_stolikow.persistence

import kotlinx.datetime.toLocalTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Restaurant {
    
    object Table: LongIdTable("restaurants") {
        val name = varchar("name", 128)
        
        val mondayOpen = varchar("monday_open", 8)
        val mondayClose = varchar("monday_close", 8)
        val tuesdayOpen = varchar("tuesday_open", 8)
        val tuesdayClose = varchar("tuesday_close", 8)
        val wednesdayOpen = varchar("wednesday_open", 8)
        val wednesdayClose = varchar("wednesday_close", 8)
        val thursdayOpen = varchar("thursday_open", 8)
        val thursdayClose = varchar("thursday_close", 8)
        val fridayOpen = varchar("friday_open", 8)
        val fridayClose = varchar("friday_close", 8)
        val saturdayOpen = varchar("saturday_open", 8)
        val saturdayClose = varchar("saturday_close", 8)
        val sundayOpen = varchar("sunday_open", 8)
        val sundayClose = varchar("sunday_close", 8)
        
        val image = varchar("image", 256)
        val map = varchar("map", 256)
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                this.name = obj.name
                this.mondayOpen = obj.openingHours.monday.from.toString()
                this.mondayClose = obj.openingHours.monday.to.toString()
                this.tuesdayOpen = obj.openingHours.tuesday.from.toString()
                this.tuesdayClose = obj.openingHours.tuesday.to.toString()
                this.wednesdayOpen = obj.openingHours.wednesday.from.toString()
                this.wednesdayClose = obj.openingHours.wednesday.to.toString()
                this.thursdayOpen = obj.openingHours.thursday.from.toString()
                this.thursdayClose = obj.openingHours.thursday.to.toString()
                this.fridayOpen = obj.openingHours.friday.from.toString()
                this.fridayClose = obj.openingHours.friday.to.toString()
                this.saturdayOpen = obj.openingHours.saturday.from.toString()
                this.saturdayClose = obj.openingHours.saturday.to.toString()
                this.sundayOpen = obj.openingHours.sunday.from.toString()
                this.sundayClose = obj.openingHours.sunday.to.toString()
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
                this.mondayOpen.toLocalTime() upTo this.mondayClose.toLocalTime(),
                this.tuesdayOpen.toLocalTime() upTo this.tuesdayClose.toLocalTime(),
                this.wednesdayOpen.toLocalTime() upTo this.wednesdayClose.toLocalTime(),
                this.thursdayOpen.toLocalTime() upTo this.thursdayClose.toLocalTime(),
                this.fridayOpen.toLocalTime() upTo this.fridayClose.toLocalTime(),
                this.saturdayOpen.toLocalTime() upTo this.saturdayClose.toLocalTime(),
                this.sundayOpen.toLocalTime() upTo this.sundayClose.toLocalTime()
            ),
            this.image,
            this.map
        )
    }
    
    @Serializable
    data class View(
        val ID: Long? = null,
        val name: String,
        val openingHours: Timetable.View,
        val image: String,
        val map: String
    )
}
