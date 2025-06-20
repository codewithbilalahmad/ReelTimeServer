package com.muhammad.reeltimeserver.data.media.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MediaByIdRequest(
    val mediaId : Int,
    val email : String
)
