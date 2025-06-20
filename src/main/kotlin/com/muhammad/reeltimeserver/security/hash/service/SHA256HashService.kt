package com.muhammad.reeltimeserver.security.hash.service

import com.muhammad.reeltimeserver.security.hash.model.HashAndSalt
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256HashService : HashingService{
    override fun generateHash(
        password: String,
        saltLength: Int,
    ): HashAndSalt {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltHex$password")
        return HashAndSalt(hash,saltHex)
    }

    override fun verifyHash(
        password: String,
        hash: String,
        salt: String,
    ): Boolean {
        return DigestUtils.sha256Hex("$salt$password") == hash
    }

}