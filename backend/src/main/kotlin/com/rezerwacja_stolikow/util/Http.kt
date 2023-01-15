@file:Suppress("unused")

package com.rezerwacja_stolikow.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import io.ktor.util.reflect.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Receives content for this request, if one is available
 */
suspend inline fun <reified T: Any> ApplicationCall.receiveOptional() =
    if (this.request.contentType() == ContentType.Application.Json) Json.decodeFromString<T>(receiveText()) else null

@DslMarker
annotation class HttpResponseWrapperDsl

data class HttpResponse<T> @HttpResponseWrapperDsl constructor(
    val status: HttpStatusCode,
    val message: T
)

//@HttpResponseWrapperDsl
suspend infix fun HttpResponse<Any>.respondTo(
    responseContext: PipelineContext<Unit, ApplicationCall>
) = when (this.message) {
    is Number, is Boolean, is Char, is String -> responseContext.context.respondText(status = this.status) {
        this.message.toString()
    }
    
    else -> responseContext.context.respond(this.status, this.message)
}

@HttpResponseWrapperDsl
suspend fun PipelineContext<Unit, ApplicationCall>.respond(
    block: PipelineContext<Unit, ApplicationCall>.() -> HttpResponse<Any>
) = block(this) respondTo this

//@HttpResponseWrapperDsl
suspend infix fun HttpResponse<Any>.respondTo(
    responseCall: ApplicationCall
) = when (this.message) {
    is Number, is Boolean, is Char, is String -> responseCall.respondText(status = this.status) {
        this.message.toString()
    }
    
    else -> responseCall.respond(this.status, this.message)
}

@JvmName("respondToT")
suspend inline infix fun <reified T: Any> HttpResponse<T>.respondTo(
    responseCall: ApplicationCall
) = when (this.message) {
    is Number, is Boolean, is Char, is String -> responseCall.respondText(status = this.status) {
        this.message.toString()
    }
    
    else -> responseCall.respond(this.status, this.message as Any, typeInfo<T>())
}


// 2xx

@HttpResponseWrapperDsl val <T> T.ok
    get() = HttpResponse(HttpStatusCode.OK, this)

@HttpResponseWrapperDsl val <T> T.created
    get() = HttpResponse(HttpStatusCode.Created, this)

@HttpResponseWrapperDsl val <T> T.accepted
    get() = HttpResponse(HttpStatusCode.Accepted, this)

@HttpResponseWrapperDsl val <T> T.noContent
    get() = HttpResponse(HttpStatusCode.NoContent, this)


// 4xx


@HttpResponseWrapperDsl val Throwable.badRequest: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.BadRequest, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.unauthorised: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.Unauthorized, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.forbidden: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.BadRequest, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.notFound: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.NotFound, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.methodNotAllowed: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.MethodNotAllowed, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.notAcceptable: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.NotAcceptable, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.requestTimeout: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.RequestTimeout, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.conflict: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.Conflict, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.gone: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.Gone, this.message ?: this::class.qualifiedName ?: "")

@Suppress("SpellCheckingInspection") @HttpResponseWrapperDsl val Throwable.unprocessableEntity: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.UnprocessableEntity, this.message ?: this::class.qualifiedName ?: "")

@HttpResponseWrapperDsl val Throwable.locked: HttpResponse<Any>
    get() = HttpResponse(HttpStatusCode.Locked, this.message ?: this::class.qualifiedName ?: "")
