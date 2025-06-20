package com.muhammad.reeltimeserver

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.muhammad.reeltimeserver.data.media.source.MongoMediaDataSource
import com.muhammad.reeltimeserver.data.user.source.MongoUserDataSource
import com.muhammad.reeltimeserver.plugins.configureMonitoring
import com.muhammad.reeltimeserver.plugins.configureRouting
import com.muhammad.reeltimeserver.plugins.configureSecurity
import com.muhammad.reeltimeserver.plugins.configureSerialization
import com.muhammad.reeltimeserver.security.hash.service.SHA256HashService
import com.muhammad.reeltimeserver.security.jwt.model.TokenConfig
import com.muhammad.reeltimeserver.security.jwt.service.JWTTokenService
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val connectionString =
        "mongodb+srv://mbilal10388:f0H5R1ujqY7Veo9S@cluster0.yr5m4w0.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    val mongoClient = MongoClient.create(connectionString)
    val database = mongoClient.getDatabase("ReelTime")
    val userCollection = MongoUserDataSource(database)
    val mediaCollection = MongoMediaDataSource(database)
    val tokenService = JWTTokenService()
    val tokenConfig = TokenConfig(
        issuer = "muhammad_issuer",
        audience = "muhammad_audience",
        expireDate = 30L * 1000L * 60L * 24L,
        secret = "muhammad_secret"
    )
    val hashingService = SHA256HashService()
    configureSecurity(tokenConfig)
    configureSerialization()
    configureMonitoring()
    configureRouting(
        mediaDataSource = mediaCollection,
        userDataSource = userCollection,
        hashingService = hashingService,
        tokenService = tokenService, tokenConfig = tokenConfig
    )
}
