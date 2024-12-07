package ru.sliva.zapp.server

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import korlibs.crypto.AES
import korlibs.crypto.CipherPadding
import korlibs.crypto.SecureRandom
import korlibs.crypto.sha512
import korlibs.io.compression.compress
import korlibs.io.compression.deflate.GZIP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.Cipher

object Server {

    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private val defaultPort = 9002

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val bytearr = SecureRandom.nextBytes(10)

        println(bytearr.encodeBase64())
        println(bytearr.compress(GZIP).encodeBase64())
        println(bytearr.sha512().base64)

        val original = "Hello, world!".encodeToByteArray()
        val key = SecureRandom.nextBytes(32)

        val encrypted = AES.encryptAesCbc(original, key, key, CipherPadding.PKCS7Padding)

        println(encrypted.encodeBase64())

        val decrypted = AES.decryptAesCbc(encrypted, key, key, CipherPadding.PKCS7Padding)

        println(decrypted.decodeToString())

        val generator = KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }

        val keypair = generator.generateKeyPair()

        val public = keypair.public as RSAPublicKey
        val private = keypair.private as RSAPrivateKey

        val encryptCipher = Cipher.getInstance("RSA").apply {
            init(Cipher.ENCRYPT_MODE, public)
        }

        // Шифрование публичным ключем
        val rsa_public = encryptCipher.doFinal(original)

        println(rsa_public.encodeBase64())

        val decryptCipher = Cipher.getInstance("RSA").apply {
            init(Cipher.DECRYPT_MODE, private)
        }

        // Расшифровка приватным ключем
        val rsa_private = decryptCipher.doFinal(rsa_public)

        println(rsa_private.decodeToString())

        val serverSocket = aSocket(selectorManager).tcp().bind(port = defaultPort)

        println("Server started at port $defaultPort")

        while(true) {
            val socket = serverSocket.accept()
            println("Accepted $socket")
        }
    }
}