package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.errors.NSEE
import com.rezerwacja_stolikow.util.fromEpochMilliseconds
import com.rezerwacja_stolikow.util.toEpochMilliseconds
import kotlinx.datetime.Clock
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
import kotlin.time.Duration

object PendingLock {
    object Table: LongIdTable("pending_lock") {
        val diningTableRestaurantID = long("dining_table__restaurant_id")
        val diningTableNumber = integer("dining_table__number")
        
        init {
            foreignKey(diningTableRestaurantID, diningTableNumber, target = DiningTable.Table.primaryKey)
        }
        
        val startDateTime = long("start_datetime")
        val endDateTime = long("end_datetime")
        val expirationDateTime = long("expiration_datetime")
    }
    
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        private var diningTableRestaurantID by Table.diningTableRestaurantID
        private var diningTableNumber by Table.diningTableNumber
        var diningTable
            get() = DiningTable.Entity.findByForeign(diningTableRestaurantID, diningTableNumber)
                ?: throw DiningTable.NSEE(diningTableRestaurantID, diningTableNumber)
            set(value) {
                diningTableRestaurantID = value.restaurant.id.value
                diningTableNumber = value.number
            }
        var startDateTime by Table.startDateTime
        var endDateTime by Table.endDateTime
        var expirationDateTime by Table.expirationDateTime
        
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                this.diningTable =
                    DiningTable.Entity.findByForeign(obj.diningTable.restaurantID, obj.diningTable.number)
                        ?: throw throw DiningTable.NSEE(diningTableRestaurantID, diningTableNumber)
                this.startDateTime = obj.bounds.from.toEpochMilliseconds()
                this.endDateTime = obj.bounds.toView().to.toEpochMilliseconds()
                this.expirationDateTime = obj.expirationDateTime!!.toEpochMilliseconds()
            }
            
            fun findConflictingLocks(
                diningTable: DiningTable.Entity,
                startDateTime: LocalDateTime,
                endDateTime: LocalDateTime
            ) = (startDateTime.toEpochMilliseconds() to endDateTime.toEpochMilliseconds()).let { (s, e) ->
                find(
                    Table.expirationDateTime less Clock.System
                        .now()
                        .toEpochMilliseconds()
                ).forEach(Entity::delete)
                
                find(
                    (Table.startDateTime less e)
                        .and(Table.endDateTime greater s)
                        .and(Table.diningTableRestaurantID eq diningTable.restaurant.id.value)
                        .and(Table.diningTableNumber eq diningTable.number)
                )
            }
        }
        
        fun toView() = View(
            this.diningTable.toView(),
            DurationDate
                .View(
                    LocalDateTime.fromEpochMilliseconds(this.startDateTime),
                    LocalDateTime.fromEpochMilliseconds(this.endDateTime)
                )
                .toAltView(),
            LocalDateTime.fromEpochMilliseconds(this.expirationDateTime)
        
        )
    }
    
    @Serializable
    data class View(
        val diningTable: DiningTable.SimpleViewModeling,
        val bounds: DurationDate.AltView,
        val expirationDateTime: LocalDateTime? = null
    )
}
