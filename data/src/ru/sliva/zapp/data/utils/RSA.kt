package ru.sliva.zapp.data.utils

import java.security.Key
import java.security.KeyPairGenerator
import javax.crypto.Cipher

object RSA {
    private val generator = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }

    fun generateKeyPair() = generator.generateKeyPair()

    fun Key.encrypt(data: ByteArray) = Cipher.getInstance("RSA").apply {
        init(Cipher.ENCRYPT_MODE, this@encrypt)
    }.doFinal(data)

    fun Key.decrypt(data: ByteArray) = Cipher.getInstance("RSA").apply {
        init(Cipher.DECRYPT_MODE, this@decrypt)
    }.doFinal(data)
}