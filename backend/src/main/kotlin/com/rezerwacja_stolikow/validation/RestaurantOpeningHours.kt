package com.rezerwacja_stolikow.validation

import com.rezerwacja_stolikow.persistence.DurationDate
import com.rezerwacja_stolikow.persistence.OpeningHours
import com.rezerwacja_stolikow.persistence.Timetable
import com.rezerwacja_stolikow.persistence.upTo
import com.rezerwacja_stolikow.util.contains
import java.time.DayOfWeek

object RestaurantOpeningHours {
    fun withOpeningHours(openingHours: Timetable.View) =
        RestaurantOpeningHoursValidator(OpeningRanges.from(openingHours))
    
    class RestaurantOpeningHoursValidator internal constructor(internal val openingHours: OpeningRanges) {
        fun isWithin(bounds: DurationDate.AltView) = OpeningRanges
            .getOverflowingRange(bounds)
            .let { range ->
                when (bounds.from.dayOfWeek) {
                    DayOfWeek.MONDAY -> range in this.openingHours.monday || OpeningRanges.previousDayRange(range) in this.openingHours.sunday
                    DayOfWeek.TUESDAY -> range in this.openingHours.tuesday || OpeningRanges.previousDayRange(range) in this.openingHours.monday
                    DayOfWeek.WEDNESDAY -> range in this.openingHours.wednesday || OpeningRanges.previousDayRange(range) in this.openingHours.tuesday
                    DayOfWeek.THURSDAY -> range in this.openingHours.thursday || OpeningRanges.previousDayRange(range) in this.openingHours.wednesday
                    DayOfWeek.FRIDAY -> range in this.openingHours.friday || OpeningRanges.previousDayRange(range) in this.openingHours.thursday
                    DayOfWeek.SATURDAY -> range in this.openingHours.saturday || OpeningRanges.previousDayRange(range) in this.openingHours.friday
                    DayOfWeek.SUNDAY -> range in this.openingHours.sunday || OpeningRanges.previousDayRange(range) in this.openingHours.saturday
                }
            }
    }
    
    internal data class OpeningRanges(
        val monday: IntRange,
        val tuesday: IntRange,
        val wednesday: IntRange,
        val thursday: IntRange,
        val friday: IntRange,
        val saturday: IntRange,
        val sunday: IntRange,
    ) {
        companion object {
            fun from(openingHours: Timetable.View) = OpeningRanges(
                getOverflowingRange(openingHours.monday),
                getOverflowingRange(openingHours.tuesday),
                getOverflowingRange(openingHours.wednesday),
                getOverflowingRange(openingHours.thursday),
                getOverflowingRange(openingHours.friday),
                getOverflowingRange(openingHours.saturday),
                getOverflowingRange(openingHours.sunday)
            )
            
            private fun getOverflowingRange(hours: OpeningHours.View) = hours.let { (f, t) ->
                if (f.hour > t.hour) f.hour until (t.hour + 24) else f.hour until t.hour
            }
            
            fun getOverflowingRange(hours: DurationDate.AltView) = hours
                .toView()
                .let { (f, t) -> getOverflowingRange(f.time upTo t.time) }
            
            fun previousDayRange(thisDayRange: IntRange) = (thisDayRange.first + 24)..(thisDayRange.last + 24)
        }
    }
}
