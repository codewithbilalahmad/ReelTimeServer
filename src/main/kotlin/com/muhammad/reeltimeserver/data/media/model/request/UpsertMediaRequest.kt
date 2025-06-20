package com.muhammad.reeltimeserver.data.media.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpsertMediaRequest(
    val mediaRequest: MediaRequest,
    val email : String
)
