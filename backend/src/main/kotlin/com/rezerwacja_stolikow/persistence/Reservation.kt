package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.serialization.PhoneNumberSerializer
import com.rezerwacja_stolikow.util.fromEpochMilliseconds
import com.rezerwacja_stolikow.util.toEpochMilliseconds
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.and

object Reservation {
    
    object Table: LongIdTable("reservation") {
        val personFirstName = varchar("person__first_name", 16)
        val personLastName = varchar("person__last_name", 40)
        val personPhoneNumber = varchar("person__phone_number", 12)
        val diningTableRestaurantID = long("dining_table__restaurant_id")
        val diningTableNumber = integer("dining_table__number")
        
        init {
            foreignKey(diningTableRestaurantID, diningTableNumber, target = DiningTable.Table.primaryKey)
        }
        
        val startDateTime = long("start_datetime")
        val endDateTime = long("end_datetime")
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        var personFirstName by Table.personFirstName
        var personLastName by Table.personLastName
        var personPhoneNumber by Table.personPhoneNumber
        private var diningTableRestaurantID by Table.diningTableRestaurantID
        private var diningTableNumber by Table.diningTableNumber
        var diningTable
            get() = DiningTable.Entity.findByForeign(diningTableRestaurantID, diningTableNumber)
                ?: throw NoSuchElementException(
                    "No such dining table: $diningTableNumber in restaurant $diningTableRestaurantID"
                )
            set(value) {
                diningTableRestaurantID = value.restaurant.id.value
                diningTableNumber = value.number
            }
        var startDateTime by Table.startDateTime
        var endDateTime by Table.endDateTime
        
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                this.personFirstName = obj.personDetails.firstName
                this.personLastName = obj.personDetails.lastName
                this.personPhoneNumber = obj.personDetails.phoneNumber
                this.diningTable =
                    DiningTable.Entity.findByForeign(obj.diningTable.restaurantID, obj.diningTable.number)
                        ?: throw NoSuchElementException(
                            "No such dining table: ${
                                obj.diningTable.restaurantID
                            } in restaurant ${
                                obj.diningTable.number
                            }"
                        )
                this.startDateTime = obj.startDateTime.toEpochMilliseconds()
                this.endDateTime = obj.endDateTime.toEpochMilliseconds()
            }
            
            fun findConflictingReservations(
                diningTable: DiningTable.Entity,
                startDateTime: LocalDateTime,
                endDateTime: LocalDateTime
            ) = (startDateTime.toEpochMilliseconds() to endDateTime.toEpochMilliseconds()).let { (s, e) ->
                find(
                    (Table.startDateTime less e)
                        .and(Table.endDateTime greater s)
                        .and(Table.diningTableRestaurantID eq diningTable.restaurant.id.value)
                        .and(Table.diningTableNumber eq diningTable.number)
                )
            }
        }
        
        fun toView() = View(
            Person.View(this.personFirstName, this.personLastName, this.personPhoneNumber),
            this.diningTable.toView(),
            LocalDateTime.fromEpochMilliseconds(this.startDateTime),
            LocalDateTime.fromEpochMilliseconds(this.endDateTime)
        )
    }
    
    @Serializable
    data class View(
        val personDetails: Person.View,
        val diningTable: DiningTable.SimpleViewModeling,
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val removalToken: String? = null
    )
}
