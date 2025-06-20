package com.muhammad.reeltimeserver.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val name : String,
    val token : String
)
