package com.muhammad.reeltimeserver.security.hash.service

import com.muhammad.reeltimeserver.security.hash.model.HashAndSalt

interface HashingService{
    fun generateHash(password : String, saltLength : Int = 23) : HashAndSalt
    fun verifyHash(password: String, hash : String, salt : String) : Boolean
}