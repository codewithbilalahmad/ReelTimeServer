package com.muhammad.reeltimeserver.route

import com.muhammad.reeltimeserver.data.auth.AuthRequest
import com.muhammad.reeltimeserver.data.auth.AuthResponse
import com.muhammad.reeltimeserver.data.user.model.User
import com.muhammad.reeltimeserver.data.user.source.UserDataSource
import com.muhammad.reeltimeserver.security.hash.service.HashingService
import com.muhammad.reeltimeserver.security.jwt.model.TokenClaim
import com.muhammad.reeltimeserver.security.jwt.model.TokenConfig
import com.muhammad.reeltimeserver.security.jwt.service.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.authRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService : TokenService,
    tokenConfig : TokenConfig
) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(
            HttpStatusCode.BadRequest
        )
        val areEmailAndPasswordEmpty = request.email.isBlank() || request.password.isBlank()
        val isValidPassword = request.password.length > 8
        if (areEmailAndPasswordEmpty || !isValidPassword) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val userWithEmail = userDataSource.getUserByEmail(request.email)
        if (userWithEmail != null) {
            call.respond(status = HttpStatusCode.Conflict, "The email is already taken")
            return@post
        }
        val hashAndSalt = hashingService.generateHash(request.password)
        val user = User(
            name = request.name,
            email = request.email,
            hash = hashAndSalt.hash,
            salt = hashAndSalt.salt,
            mediaList = ArrayList()
        )
        val wasAcknowledge = userDataSource.insertUser(user)
        if(!wasAcknowledge){
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
    post("login"){
        val request = call.receiveNullable<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val areEmailAndPasswordBlank = request.email.isBlank() || request.password.isBlank()
        if(areEmailAndPasswordBlank){
            call.respond(status = HttpStatusCode.Conflict, message = "Wrong email or password")
            return@post
        }
        val user = userDataSource.getUserByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "Wrong email or password")
            return@post
        }
        val isValidPassword = hashingService.verifyHash(
            password = request.password, salt = user.salt, hash = user.hash
        )
        if(!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "Wrong email or password")
            return@post
        }
        val token = tokenService.generate(tokenConfig = tokenConfig,TokenClaim(
            name = "userId", value = user.id.toString()
        ))
        call.respond(status = HttpStatusCode.OK, message = AuthResponse(
            name = user.name, token = token
        ))
    }
    authenticate {
        get("authenticate"){
            call.respond(HttpStatusCode.OK)
        }
    }
}