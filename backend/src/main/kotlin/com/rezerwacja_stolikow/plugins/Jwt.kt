@file:Suppress("unused")

package com.rezerwacja_stolikow.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.rezerwacja_stolikow.errors.AuthenticationException
import com.rezerwacja_stolikow.errors.AuthorizationException
import com.rezerwacja_stolikow.errors.ThrowableFactory
import com.rezerwacja_stolikow.util.toEpochMilliseconds
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import java.util.*
import kotlin.time.Duration

object Jwt {
    
    const val KEY = "jwt-authorization"
    
    @Suppress("FunctionName")
    @ThrowableFactory
    fun AENone() = AuthenticationException("No payload!")
    
    @Suppress("FunctionName")
    @ThrowableFactory
    fun AEType() = AuthorizationException("Wrong token type")
    
    object Subjects {
        const val LOCK = "lock"
        const val CANCEL = "cancel"
        const val ACCESS = "access"
    }
    
    object Claims {
        const val DINING_TABLE = "diningTable"
        const val BOUNDS = "bounds"
        const val CLIENT = "client"
    }
    
    private var isInit = false
    
    private var _secret = ""
    val secret
        get() = _secret
    
    private var _domain = ""
    val domain
        get() = _domain
    
    private var _audience = ""
    val audience
        get() = _audience
    
    private var _realm = ""
    val realm
        get() = _realm
    
    fun init(
        secret: String,
        domain: String,
        audience: String,
        realm: String
    ) {
        if (isInit) throw IllegalStateException("Already initialized")
        
        _secret = secret
        _domain = domain
        _audience = audience
        _realm = realm
        
        isInit = true
    }
    
    fun create(
        lifetime: Duration,
        configuration: JWTCreator.Builder.() -> Unit
    ) = JWT
        .create()
        .withIssuer(domain)
        .withAudience(audience)
        .withExpiresAt(
            Date(
                Clock.System
                    .now()
                    .plus(lifetime)
                    .toEpochMilliseconds()
            )
        )
        .apply(configuration)
        .sign(Algorithm.HMAC256(secret))!!
    
    fun create(
        expiration: LocalDateTime,
        configuration: JWTCreator.Builder.() -> Unit
    ) = JWT
        .create()
        .withIssuer(domain)
        .withAudience(audience)
        .withExpiresAt(Date(expiration.toEpochMilliseconds()))
        .apply(configuration)
        .sign(Algorithm.HMAC256(secret))!!
}
