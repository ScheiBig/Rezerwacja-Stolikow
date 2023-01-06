@file:Suppress("ClassName")

package com.rezerwacja_stolikow.validation

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class `Phone Validator Tests` {
    
    @Test
    fun `test unknown beginning values`() {
        val randomEnding = {
            CharArray(7) {
                (0..9)
                    .random()
                    .digitToChar()
            }.concatToString()
        }
        
        val invalidBeginningNumbers = listOf(
            10, 11, 19, 20, 21, 27, 28, 30, 31, 35, 36, 37, 38, 40, 49, 64, 90, 92, 93, 96, 97, 98, 99
        )
            .map { "$it${randomEnding()}" }
            .map(String::toLong)
        
        invalidBeginningNumbers.forEachIndexed { i, raw ->
            assertThrows<NumberFormatException>("at index $i, value: $raw") { PhoneNumber.toString(raw) }
        }
    }
    
    @Test
    fun `test invalid length values`() {
        12_345_67_8L.let {
            assertThrows<NumberFormatException> { PhoneNumber.toString(it) }
        }
        
        889_900_112_2L.let {
            assertThrows<NumberFormatException> { PhoneNumber.toString(it) }
        }
    }
    
    @Test
    fun `test parsing results`() {
        val properNumbers = mapOf(
            12_345_67_89L to "12 345 67 89",
            26_154_37_89L to "26 154 37 89",
            396_285_174L to "396 285 174",
            888_999_000L to "888 999 000"
        )
        
        properNumbers
            .toList()
            .forEachIndexed { i, (raw, parsed) ->
                assertDoesNotThrow("at index $i, value $raw") { PhoneNumber.toString(raw) }
                assertEquals(parsed, PhoneNumber.toString(raw), "at index $i, value $raw")
            }
    }
}
