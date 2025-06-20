package com.muhammad.reeltimeserver.security.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.muhammad.reeltimeserver.security.jwt.model.TokenClaim
import com.muhammad.reeltimeserver.security.jwt.model.TokenConfig
import java.util.Date

class JWTTokenService : TokenService {
    override fun generate(
        tokenConfig: TokenConfig,
       vararg claims: TokenClaim,
    ): String {
        val token = JWT.create().withIssuer(tokenConfig.issuer).withAudience(tokenConfig.audience).withExpiresAt(
            Date(System.currentTimeMillis() + tokenConfig.expireDate))
        claims.forEach { tokenClaim ->
            token.withClaim(tokenClaim.name, tokenClaim.value)
        }
        return token.sign(Algorithm.HMAC256(tokenConfig.secret))
    }
}