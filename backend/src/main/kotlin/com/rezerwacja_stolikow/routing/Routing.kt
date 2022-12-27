package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.*
import com.rezerwacja_stolikow.persistence.restaurantRoutes
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*


fun Application.configureRouting() {
    install(StatusPages) {
        /// 401 Unauthorized
        exception<AuthenticationException> { call, cause ->
            cause.unautorised respondTo call
        }
        /// 403 Forbidden
        exception<AuthorizationException> { call, cause ->
            cause.forbidden respondTo call
        }
        /// 404 Not found
        exception<NoSuchElementException> { call, cause ->
            cause.notFound respondTo call
        }
        /// 408 Request timeout
        exception<LockTimeoutException> { call, cause ->
            cause.requestTimeout respondTo call
        }
        /// 409 Conflict
        exception<DataTakenException> { call, cause ->
            cause.conflict respondTo call
        }
        /// 422 Unprocessable entity
        exception<NumberFormatException> { call, cause ->
            cause.unprocessableEntity respondTo call
        }
        exception<IllegalArgumentException> { call, cause ->
            cause.unprocessableEntity respondTo call
        }
        /// 423 Locked
        exception<DataLockedException> { call, cause ->
            cause.locked respondTo call
        }
    }
    
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
