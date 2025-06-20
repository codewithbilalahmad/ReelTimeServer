package com.muhammad.reeltimeserver.data.media.source

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.muhammad.reeltimeserver.data.media.model.Media
import com.muhammad.reeltimeserver.data.user.model.User
import kotlinx.coroutines.flow.firstOrNull

class MongoMediaDataSource(
    database: MongoDatabase,
) : MediaDataSource {
    private val userCollection = database.getCollection<User>("users")
    override suspend fun getMediaByIdAndUser(
        mediaId: Int,
        email: String,
    ): Media? {
        return userCollection.find(eq(User::email.name, email)).firstOrNull()?.mediaList?.find {
            it.mediaId == mediaId
        }
    }

    override suspend fun getLikedMediaListOfUser(email: String): List<Media>? {
        return userCollection.find(eq(User::email.name, email))
            .firstOrNull()?.mediaList?.filter { it.isLiked }
    }

    override suspend fun getBookmarkMediaListOfUser(email: String): List<Media>? {
        return userCollection.find(eq(User::email.name, email))
            .firstOrNull()?.mediaList?.filter { it.isBookmarked }
    }

    override suspend fun upsertMediaToUser(
        email: String,
        media: Media,
    ): Boolean {
        val user = userCollection.find(
            eq(User::email.name, email)
        ).firstOrNull() ?: return false
        val upsertedMediaList = user.mediaList.map { userMedia ->
            if (userMedia.mediaId == media.mediaId) media else userMedia
        }.toMutableList()
        if (!upsertedMediaList.contains(media)) {
            upsertedMediaList.add(media)
        }
        val query = eq(User::email.name, email)
        val updates = Updates.combine(
            Updates.set(User::mediaList.name, upsertedMediaList)
        )
        val options = UpdateOptions().upsert(true)
        return userCollection.updateOne(query, updates, options).wasAcknowledged()
    }

    override suspend fun deleteMediaFromUser(
        email: String,
        mediaId: Int,
    ): Boolean {
        val user = userCollection.find(
            eq(User::email.name, email)
        ).firstOrNull() ?: return false
        val mediaToRemove = user.mediaList.find { media ->
            media.mediaId == mediaId
        }
        if (mediaToRemove != null) user.mediaList.remove(mediaToRemove) else false
        val query = eq(User::email.name, email)
        val updates = Updates.combine(
            Updates.set(User::mediaList.name, user.mediaList)
        )
        val options = UpdateOptions().upsert(true)
        return userCollection.updateOne(query, updates, options).wasAcknowledged()
    }

}