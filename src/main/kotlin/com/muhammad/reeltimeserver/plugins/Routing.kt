package com.muhammad.reeltimeserver.plugins

import com.muhammad.reeltimeserver.data.media.source.MediaDataSource
import com.muhammad.reeltimeserver.data.user.source.UserDataSource
import com.muhammad.reeltimeserver.route.authRouting
import com.muhammad.reeltimeserver.route.mediaRouting
import com.muhammad.reeltimeserver.security.hash.service.HashingService
import com.muhammad.reeltimeserver.security.jwt.model.TokenConfig
import com.muhammad.reeltimeserver.security.jwt.service.TokenService
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(
    mediaDataSource: MediaDataSource,
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        get("/"){
            call.respondText("Welcome To Muhammad ReelTime Server😎😎😎")
        }
        authRouting(
            hashingService = hashingService,
            userDataSource = userDataSource,
            tokenConfig = tokenConfig,
            tokenService = tokenService
        )
        mediaRouting(mediaDataSource = mediaDataSource, userDataSource = userDataSource)
    }
}