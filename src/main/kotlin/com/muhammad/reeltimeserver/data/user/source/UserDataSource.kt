package com.muhammad.reeltimeserver.data.user.source

import com.muhammad.reeltimeserver.data.user.model.User

interface UserDataSource {
    suspend fun getUserByEmail(email : String) : User?
    suspend fun insertUser(user : User) : Boolean
    suspend fun updateUser(user : User) : Boolean
}