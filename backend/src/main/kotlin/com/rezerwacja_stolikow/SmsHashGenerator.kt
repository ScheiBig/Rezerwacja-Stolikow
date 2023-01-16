package com.rezerwacja_stolikow

import org.hashids.HashIDable
import org.hashids.HashIDs

object SmsHashGenerator: HashIDable by HashIDs("another_simple_good_old_salt", alphabet = "0123456789abcdef") {
    fun encodePhone(phone: Long) = this.encode(phone)
    
    fun decodePhone(hash: String) = this
        .decode(hash)
        .first()
}
