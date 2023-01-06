package com.rezerwacja_stolikow.validation

import com.rezerwacja_stolikow.util.with

object Date {
    private val nfe = NumberFormatException("Invalid date!")
    
    fun toString(date: Int) = date
        .let { i -> if (i < 0) throw nfe else i }
        .toString()
        .let { s -> if (s.length != 8) throw nfe else s }
        .let { s ->
            (s.substring(0..3) to s.substring(4..5) with s.substring(6..7)).let { (y, m, d) ->
                when (m.toInt()) {
                    1, 3, 5, 7, 8, 10, 12 -> if (d.toInt() !in 1..31) throw nfe
                    else y to m with d
                    
                    4, 6, 9, 11 -> if (d.toInt() !in 1..30) throw nfe
                    else y to m with d
                    
                    2 -> when {
                        y.toInt() % 4 == 0 && y.toInt() % 400 != 0 -> if (d.toInt() !in 1..29) throw nfe
                        else y to m with d
                        
                        else -> if (d.toInt() !in 1..28) throw nfe
                        else y to m with d
                    }
                    
                    else -> throw nfe
                }
            }
        }
        .let { (y, m, d) -> "$y-$m-$d" }
}
