@file:Suppress("ClassName")

package com.rezerwacja_stolikow.validation

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class `Time Validator Tests` {
    
    @Test
    fun `test boundary values`() {
        0.let {
            assertDoesNotThrow { Time.toString(it) }
            assertEquals("00:00", Time.toString(it))
        }
        23_59.let {
            assertDoesNotThrow { Time.toString(it) }
            assertEquals("23:59", Time.toString(it))
        }
    }
    
    @Test
    fun `test out of boundary values`() {
        (-1).let {
            assertThrows<NumberFormatException> { Time.toString(it) }
        }
        24_00.let {
            assertThrows<NumberFormatException> { Time.toString(it) }
        }
    }
    
    @Test
    fun `test invalid minute values`() {
        12_69.let {
            assertThrows<NumberFormatException> { Time.toString(it) }
        }
    }
    
    @Test
    fun `test parsing results`() {
        val properTimes = mapOf(
            5 to "00:05",
            12 to "00:12",
            4_20 to "04:20",
            6_09 to "06:09",
            12_20 to "12:20",
            21_37 to "21:37"
        )
        
        properTimes
            .toList()
            .forEachIndexed { i, (raw, parsed) ->
                assertDoesNotThrow("at index $i, value $raw") { Time.toString(raw) }
                assertEquals(parsed, Time.toString(raw), "at index $i, value $raw")
            }
    }
}
