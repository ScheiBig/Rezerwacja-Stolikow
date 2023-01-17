package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.persistence.SmsCodes
import com.rezerwacja_stolikow.plugins.Jwt
import com.rezerwacja_stolikow.util.div
import com.rezerwacja_stolikow.util.ok
import com.rezerwacja_stolikow.util.receiveOptional
import com.rezerwacja_stolikow.util.respondTo
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Routing.smsCheckingRoutes() {
    route(SMS_CHECKING / RESERVATIONS) {
        get {
            val phoneNumber =
                this.call.receiveOptional<PhoneNumber>() ?: throw IllegalArgumentException("PhoneNumber is missing")
            
            SmsCodes.Memory.createNewUniqueCode(
                phoneNumber.phoneNumber
                    .replace(" ", "")
                    .toLong()
            ).ok respondTo this.call
        }
        
        
        post {
            val decode = try {
                SmsCodes.Memory.getLastCodeOwner(this.call
                    .receiveText()
                    .let { it.ifBlank { throw Jwt.AENone() } }
                    .toInt())
            } catch (_: Exception) {
                null
            } ?: throw Jwt.AEType()
            
            Jwt.create {
                withSubject(Jwt.Subjects.ACCESS)
                withClaim(Jwt.Claims.PHONE_NUMBER, decode.toString())
            }.ok respondTo this.call
        }
    }
}
