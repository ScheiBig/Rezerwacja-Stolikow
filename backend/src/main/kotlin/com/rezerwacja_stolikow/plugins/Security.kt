package com.rezerwacja_stolikow.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    initJwt(this)
    
    authentication {
        jwt(Jwt.KEY) {
            
            this.realm = Jwt.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(Jwt.secret))
                    .withAudience(Jwt.audience)
                    .withIssuer(Jwt.domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(Jwt.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
    
}

private fun initJwt(app: Application) {
    val secret = app.environment.config
        .property("jwt.secret")
        .getString()
    val domain = app.environment.config
        .property("jwt.domain")
        .getString()
    val audience = app.environment.config
        .property("jwt.audience")
        .getString()
    val realm = app.environment.config
        .property("jwt.realm")
        .getString()
    
    Jwt.init(secret, domain, audience, realm)
}
