package com.rezerwacja_stolikow

import org.hashids.HashIDable
import org.hashids.HashIDs

object HashGenerator: HashIDable by HashIDs("basic_simple_plain_salt", 4) {
    fun encodeString(str: String) = this.encode(
        *str
            .map(Char::code)
            .map(Int::toLong)
            .toLongArray()
    )
    
    fun decodeString(hash: String) = this
        .decode(hash)
        .map(Long::toInt)
        .map(Int::toChar)
        .toCharArray()
        .concatToString()
}
