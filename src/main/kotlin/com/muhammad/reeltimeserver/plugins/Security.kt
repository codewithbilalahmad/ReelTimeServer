package com.muhammad.reeltimeserver.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.muhammad.reeltimeserver.security.jwt.model.TokenConfig
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(tokenConfig : TokenConfig) {
    val jwtRealm = "Muhammad ReelTime Server"
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(tokenConfig.secret))
                    .withAudience(tokenConfig.audience).withIssuer(tokenConfig.issuer).build()
            )
            validate { jwtCredential ->
                if (jwtCredential.payload.audience.contains(tokenConfig.audience)) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}