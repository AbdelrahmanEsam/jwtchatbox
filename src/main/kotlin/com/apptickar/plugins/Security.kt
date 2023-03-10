package com.apptickar.plugins

import com.apptickar.security.token.TokenConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get

fun Application.configureSecurity(config: TokenConfig = get()) {
    authentication {
            jwt {
                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
                verifier(
                    JWT.require(Algorithm.HMAC256(config.secret))
                        .withAudience(config.audience)
                        .withIssuer(this@configureSecurity.environment.config.property("jwt.issuer").getString())
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
                }
            }
        }

}
