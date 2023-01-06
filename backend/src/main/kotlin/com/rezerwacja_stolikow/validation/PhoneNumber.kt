package com.rezerwacja_stolikow.validation

object PhoneNumber {
    private val nfe = NumberFormatException("Invalid phone number!")
    
    fun toString(phoneNumber: Long) = phoneNumber
        .let { l -> if (l < 0) throw nfe else l }
        .toString()
        .let { s -> if (s.length != 9) throw nfe else s }
        .let { s ->
            when (s.substring(0..1)) {
                in mobileNumbers, in VoIPNumbers -> "${
                    s.substring(0..2)
                } ${
                    s.substring(3..5)
                } ${
                    s.substring(6..8)
                }"
                
                in fixedLineNumbers, in ministryNumbers -> "${
                    s.substring(0..1)
                } ${
                    s.substring(2..4)
                } ${
                    s.substring(5..6)
                } ${
                    s.substring(7..8)
                }"
                
                else -> throw nfe
            }
        }
    
    private val mobileNumbers = listOf("45", "50", "51", "53", "57", "60", "66", "69", "72", "73", "78", "79", "88")
    private val VoIPNumbers = listOf("39")
    private val fixedLineNumbers = listOf(
        listOf("12", "13", "14", "15", "16", "17", "18"),
        listOf("22", "23", "24", "25", "29"),
        listOf("32", "33", "34"),
        listOf("41", "42", "43", "44", "46", "48"),
        listOf("52", "54", "55", "56", "58", "59"),
        listOf("61", "62", "63", "65", "67", "68"),
        listOf("71", "74", "75", "76", "77"),
        listOf("81", "82", "83", "84", "85", "86", "87", "89"),
        listOf("91", "94", "95")
    ).flatten()
    private val ministryNumbers = listOf("26", "47")
}
