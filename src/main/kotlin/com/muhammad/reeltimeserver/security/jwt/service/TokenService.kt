package com.muhammad.reeltimeserver.security.jwt.service

import com.muhammad.reeltimeserver.security.jwt.model.*

interface TokenService {
    fun generate(tokenConfig: TokenConfig,vararg claims: TokenClaim): String
}