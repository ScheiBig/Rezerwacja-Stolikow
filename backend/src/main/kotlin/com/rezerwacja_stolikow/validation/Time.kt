package com.rezerwacja_stolikow.validation

object Time {
    private val nfe = NumberFormatException("Invalid time!")
    
    fun toString(time: Int) = time
        .let { i -> if (i !in 0..2359 || (i / 10 % 10) !in 0..5) throw nfe else i }
        .toString()
        .let { s ->
            when (s.length) {
                1 -> "00:0$s"
                2 -> "00:$s"
                3 -> "0${s.first()}:${s.substring(1..2)}"
                4 -> "${s.substring(0..1)}:${s.substring(2..3)}"
                
                else -> throw nfe
            }
        }
}
