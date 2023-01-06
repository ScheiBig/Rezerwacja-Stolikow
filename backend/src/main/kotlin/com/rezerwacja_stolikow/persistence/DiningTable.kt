package com.rezerwacja_stolikow.persistence

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object DiningTable {
    
    object Table: LongIdTable("dining_table") {
        val restaurant = reference("restaurant", Restaurant.Table)
        val number = integer("number")
        val byWindow = bool("by_window")
        val outside = bool("outside")
        val smokingAllowed = bool("smoking_allowed")
        val chairs = integer("chairs")
        val mapLocationX = integer("mloc_x")
        val mapLocationY = integer("mloc_y")
        val mapLocationW = integer("mloc_w")
        val mapLocationH = integer("mloc_h")
    }
    
    class Entity(id: EntityID<Long>): LongEntity(id) {
        companion object: LongEntityClass<Entity>(Table) {
            fun fromView(obj: View) = new {
                this.restaurant = Restaurant.Entity.findById(obj.restaurantID)!!
                this.number = obj.number
                this.byWindow = obj.byWindow
                this.outside = obj.outside
                this.smokingAllowed = obj.smokingAllowed
                this.chairs = obj.chairs
                this.mapLocationX = obj.mapLocation.x
                this.mapLocationY = obj.mapLocation.y
                this.mapLocationW = obj.mapLocation.w
                this.mapLocationH = obj.mapLocation.h
            }
        }
        
        var restaurant by Restaurant.Entity referencedOn Table.restaurant
        var number by Table.number
        var byWindow by Table.byWindow
        var outside by Table.outside
        var smokingAllowed by Table.smokingAllowed
        var chairs by Table.chairs
        var mapLocationX by Table.mapLocationX
        var mapLocationY by Table.mapLocationY
        var mapLocationW by Table.mapLocationW
        var mapLocationH by Table.mapLocationH
        
        fun toView() = View(
            this.restaurant.id.value,
            this.number,
            this.byWindow,
            this.outside,
            this.smokingAllowed,
            this.chairs,
            MapLocation.View(this.mapLocationX, this.mapLocationY, this.mapLocationW, this.mapLocationH)
        )
    }
    
    @Serializable
    data class View(
        override val restaurantID: Long,
        override val number: Int,
        override val byWindow: Boolean,
        override val outside: Boolean,
        override val smokingAllowed: Boolean,
        val chairs: Int,
        val mapLocation: MapLocation.View
    ): SimpleViewModeling
    
    @Serializable(with = SimpleViewModelingSerializer::class)
    sealed interface SimpleViewModeling {
        val restaurantID: Long
        val number: Int
        val byWindow: Boolean?
        val outside: Boolean?
        val smokingAllowed: Boolean?
    }
    
    object SimpleViewModelingSerializer: KSerializer<SimpleViewModeling> {
        override val descriptor = PrimitiveSerialDescriptor("DiningTable.SimpleViewModeling", PrimitiveKind.STRING)
        private val simpleViewSerializer = SimpleView.serializer()
        override fun deserialize(decoder: Decoder): SimpleViewModeling {
            return simpleViewSerializer.deserialize(decoder)
        }
        
        override fun serialize(encoder: Encoder, value: SimpleViewModeling) {
            val view = SimpleView(
                restaurantID = value.restaurantID,
                number = value.number,
                byWindow = value.byWindow,
                outside = value.outside,
                smokingAllowed = value.smokingAllowed
            )
            simpleViewSerializer.serialize(encoder, view)
        }
        
    }
    
    @Serializable
    data class SimpleView(
        override val restaurantID: Long,
        override val number: Int,
        override val byWindow: Boolean? = null,
        override val outside: Boolean? = null,
        override val smokingAllowed: Boolean? = null
    ): SimpleViewModeling
}
