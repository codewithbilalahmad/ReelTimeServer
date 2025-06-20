package com.muhammad.reeltimeserver.route

import com.muhammad.reeltimeserver.data.media.mapper.toMedia
import com.muhammad.reeltimeserver.data.media.mapper.toMediaResponse
import com.muhammad.reeltimeserver.data.media.model.request.MediaByIdRequest
import com.muhammad.reeltimeserver.data.media.model.request.UpsertMediaRequest
import com.muhammad.reeltimeserver.data.media.source.MediaDataSource
import com.muhammad.reeltimeserver.data.user.source.UserDataSource
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.mediaRouting(
    mediaDataSource : MediaDataSource,
    userDataSource  : UserDataSource
){
    get("get-liked-media-list/{email}"){
        val email = call.parameters["email"]
        if(email.isNullOrBlank()){
            call.respond(status = HttpStatusCode.BadRequest, message = "Email is null or blank")
            return@get
        }
        val user = userDataSource.getUserByEmail(email)
        if(user == null){
            call.respond(status = HttpStatusCode.NotFound, message = "User not found")
            return@get
        }
        val likeMediaList =  mediaDataSource.getLikedMediaListOfUser(email)
        if(likeMediaList == null){
            call.respond(HttpStatusCode.NoContent,"No liked media by user!")
            return@get
        }
        call.respond(status = HttpStatusCode.OK, message = likeMediaList.map { it.toMediaResponse() })
    }
    get("get-bookmark-media-list/{email}"){
        val email = call.parameters["email"]
        if(email.isNullOrBlank()){
            call.respond(status = HttpStatusCode.BadRequest, message = "Email is null or blank")
            return@get
        }
        val user = userDataSource.getUserByEmail(email)
        if(user == null){
            call.respond(status = HttpStatusCode.NotFound, message = "User not found")
            return@get
        }
        val bookmarkedMediaList =  mediaDataSource.getLikedMediaListOfUser(email)
        if(bookmarkedMediaList == null){
            call.respond(HttpStatusCode.NoContent,"No Bookmarked media by user!")
            return@get
        }
        call.respond(status = HttpStatusCode.OK, message = bookmarkedMediaList.map { it.toMediaResponse() })
    }
    get("get-media-by-id/{id}/{email}"){
        val id = call.parameters["id"]
        val email = call.parameters["email"]
        if(id.isNullOrBlank() || email.isNullOrBlank()){
            call.respond(HttpStatusCode.BadRequest, "Email and media is must be specified")
            return@get
        }
        val mediaId = id.toIntOrNull()
        if(mediaId == null){
            call.respond(HttpStatusCode.BadRequest, "Media id must be integer")
            return@get
        }
        val user = userDataSource.getUserByEmail(email)
        if(user == null){
            call.respond(HttpStatusCode.NotFound, "User not found")
            return@get
        }
        val mediaFromDb = mediaDataSource.getMediaByIdAndUser(mediaId, email = email)
        if(mediaFromDb == null){
            call.respond(HttpStatusCode.NotFound, "Media not found")
            return@get
        }
        call.respond(HttpStatusCode.OK, message = mediaFromDb.toMediaResponse())
    }
    post("delete-media-from-user"){
        val request = call.receiveNullable<MediaByIdRequest>() ?: return@post call.respond(
            HttpStatusCode.BadRequest)
        if(request.email.isBlank()){
            call.respond(HttpStatusCode.BadRequest, "Email must be specified")
            return@post
        }
        val user = userDataSource.getUserByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.NotFound, "User not found")
            return@post
        }
        val wasAcknowlegde = mediaDataSource.deleteMediaFromUser(email = request.email, mediaId = request.mediaId)
        if(!wasAcknowlegde){
            call.respond(HttpStatusCode.NotFound, "Media not found")
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
    post("upsert-media-to-user"){
        val request = call.receiveNullable<UpsertMediaRequest>() ?: return@post call.respond(
            HttpStatusCode.BadRequest)
        if(request.email.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Email must be specified")
            return@post
        }
        val user = userDataSource.getUserByEmail(request.email)
        if(user == null){
            call.respond(
                HttpStatusCode.Conflict, "No such user"
            )
            return@post
        }
        val wasAcknowlegde = mediaDataSource.upsertMediaToUser(email = request.email, media = request.mediaRequest.toMedia())
        if(!wasAcknowlegde){
            call.respond(HttpStatusCode.Conflict, "Media upsert failed")
            return@post
        }
        call.respond(HttpStatusCode.OK, message = "Media Upsert Success!")
    }
}