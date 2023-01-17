package com.rezerwacja_stolikow.persistence

import com.rezerwacja_stolikow.util.now
import com.rezerwacja_stolikow.util.plus
import kotlinx.datetime.LocalDateTime
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object SmsCodes {
    
    object Memory {
        private val _lock = ReentrantLock()
        private val _map = mutableMapOf<Long, View>()
        
        fun createNewUniqueCode(
            phoneNumber: Long,
            lifetime: Duration = (2.minutes + 5.seconds)
        ) = _lock.withLock {
            var code: View? = null
            while (code == null) {
                code = View.newRandomCode(lifetime)
                if (_map
                        .filter { (_, v) -> v.lastCode == code?.lastCode }
                        .isNotEmpty()
                ) {
                    code = null
                } else {
                    _map[phoneNumber] = code
                    return@withLock code.lastCode
                }
            }
        }
        
        fun getLastCodeOwner(code: Int) = _lock.withLock {
            val entry = _map.filter { (_, v) -> v.lastCode == code }.entries
                .firstOrNull()
            if (entry != null && entry.value.expiration < LocalDateTime.now) {
                _map.remove(entry.key)
                null
            } else entry?.key
        }
    }
    
    data class View internal constructor(
        val lastCode: Int,
        val expiration: LocalDateTime
    ) {
        companion object {
            internal fun newRandomCode(lifetime: Duration) =
                View((100_000..999_999).random(), LocalDateTime.now + lifetime)
        }
    }
}
