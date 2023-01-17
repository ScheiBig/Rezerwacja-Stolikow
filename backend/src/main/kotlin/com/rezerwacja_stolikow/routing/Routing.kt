package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.errors.*
import com.rezerwacja_stolikow.util.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    install(StatusPages) {
        /// 400 Bad request
        exception<BadRequestException> { call, cause ->
            cause.badRequest respondTo call
        }
        /// 401 Unauthorized
        exception<AuthenticationException> { call, cause ->
            cause.unauthorised respondTo call
        }
        /// 403 Forbidden
        exception<AuthorizationException> { call, cause ->
            cause.forbidden respondTo call
        }
        /// 404 Not found
        exception<NoSuchElementException> { call, cause ->
            cause.notFound respondTo call
        }
        /// 405 Method not allowed
        status(HttpStatusCode.NotFound) { call, _ ->
            Throwable("Unrecognised URL or HTTP method").methodNotAllowed respondTo call
        }
        /// 408 Request timeout
        exception<LockTimeoutException> { call, cause ->
            cause.requestTimeout respondTo call
        }
        /// 409 Conflict
        exception<DataTakenException> { call, cause ->
            cause.conflict respondTo call
        }
        /// 410 Gone
        exception<DataSpoiledException> { call, cause ->
            cause.gone respondTo call
        }
        /// 412 Precondition failed
        exception<NullPointerException> { call, cause ->
            cause.preconditionFailed respondTo call
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
        diningTableRoutes()
        restaurantRoutes()
        pendingLockRoutes()
        reservationRoutes()
        smsCheckingRoutes()
        imageRoutes()
        hashingRoutes()
    }
}
