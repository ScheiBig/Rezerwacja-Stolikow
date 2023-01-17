package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.HashGenerator
import com.rezerwacja_stolikow.util.ok
import com.rezerwacja_stolikow.util.respondTo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.hashingRoutes() {
    route("test_hashing") {
        get("encode") {
            val param = this.call.parameters
            val id = param.getOrFail(ID)
            
            HashGenerator.encodeString(id).ok respondTo this.call
        }
        get("decode") {
            val param = this.call.parameters
            val id = param.getOrFail(ID)
            
            HashGenerator.decodeString(id).ok respondTo this.call
        }
    }
}
