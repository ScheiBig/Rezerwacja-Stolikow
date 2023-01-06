@file:Suppress("ClassName")

package com.rezerwacja_stolikow.validation

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class `Date Validator Tests` {
    @Test
    fun `test boundary values`() {
        1000_01_01.let {
            assertDoesNotThrow { Date.toString(it) }
            assertEquals("1000-01-01", Date.toString(it))
        }
        9999_12_12.let {
            assertDoesNotThrow { Date.toString(it) }
            assertEquals("9999-12-12", Date.toString(it))
        }
    }
    
    @Test
    fun `test invalid length values`() {
        999_12_12.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
        10000_01_01.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
    }
    
    @Test
    fun `test out of boundary month values`() {
        1970_00_01.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
        1970_13_01.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
    }
    
    @Test
    fun `test leap year values`() {
        2000_02_29.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
        2001_02_29.let {
            assertThrows<NumberFormatException> { Date.toString(it) }
        }
        2024_02_29.let {
            assertDoesNotThrow { Date.toString(it) }
            assertEquals("2024-02-29", Date.toString(it))
        }
    }
    
    @Test
    fun `test out of boundary day values`() {
        val lowerBoundDates = listOf(
            1970_01_00,
            1970_02_00,
            1970_03_00,
            1970_04_00,
            1970_05_00,
            1970_06_00,
            1970_07_00,
            1970_08_00,
            1970_09_00,
            1970_10_00,
            1970_11_00,
            1970_12_00
        )
        
        lowerBoundDates.forEachIndexed { i, raw ->
            assertThrows<NumberFormatException>("at index $i, value $raw") { Date.toString(raw) }
        }
        
        
        val upperBoundDates = listOf(
            1970_01_32,
            // Tested in `test leap year values`()
            1970_03_32,
            1970_04_31,
            1970_05_32,
            1970_06_31,
            1970_07_32,
            1970_08_32,
            1970_09_31,
            1970_10_32,
            1970_11_31,
            1970_12_32
        )
        
        upperBoundDates.forEachIndexed { i, raw ->
            assertThrows<NumberFormatException>("at index $i, value $raw") { Date.toString(raw) }
        }
    }
    
    @Test
    fun `test parsing results`() {
        val properDates = mapOf(
            1410_07_15 to "1410-07-15", 2020_11_30 to "2020-11-30", 1970_01_01 to "1970-01-01"
        )
        
        properDates
            .toList()
            .forEachIndexed { i, (raw, parsed) ->
                assertDoesNotThrow("at index $i, value: $raw") { Date.toString(raw) }
                assertEquals(parsed, Date.toString(raw))
            }
    }
}
