package ru.sliva.zapp.server

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import korlibs.crypto.AES
import korlibs.crypto.CipherPadding
import korlibs.crypto.SecureRandom
import korlibs.crypto.sha512
import korlibs.encoding.base64
import korlibs.io.compression.compress
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sliva.zapp.data.Entity
import ru.sliva.zapp.data.User
import ru.sliva.zapp.data.utils.RSA
import ru.sliva.zapp.data.utils.RSA.decrypt
import ru.sliva.zapp.data.utils.RSA.encrypt
import ru.sliva.zapp.data.utils.writeString

object Server {

    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private val defaultPort = 9002

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val user = User(
            1,
            "Sliva",
            "sliva",
            "Sliva is a cool guy",
            0,
        )

        var bytes = Json.encodeToString(user).encodeToByteArray()

        bytes = bytes.compress(GZIP)
        bytes = bytes.uncompress(GZIP)

        val buffer = Buffer()

        buffer.writeString("Hello, world!")

        val bytes2 = buffer.readBytes().compress(GZIP)

        val buffer2 = Buffer()

        buffer2.write(bytes2.uncompress(GZIP))

        println(buffer2.readString())

        println(Json.decodeFromString<User>(bytes.decodeToString()))

        val bytearr = SecureRandom.nextBytes(10)

        println(bytearr.encodeBase64())
        println(bytearr.compress(GZIP).base64)
        println(bytearr.sha512().base64)

        val original = "Hello, world!".encodeToByteArray()
        val key = SecureRandom.nextBytes(32)

        val encrypted = AES.encryptAesCbc(original, key, key, CipherPadding.PKCS7Padding)

        println(encrypted.encodeBase64())

        val decrypted = AES.decryptAesCbc(encrypted, key, key, CipherPadding.PKCS7Padding)

        println(decrypted.decodeToString())

        val keypair = RSA.generateKeyPair()

        val public = keypair.public
        val private = keypair.private

        println("Длина публичного ключа ${public.encoded.size}")

        // Шифрование публичным ключем
        val rsa_public = public.encrypt(original)

        println(rsa_public.encodeBase64())

        // Расшифровка приватным ключем
        val rsa_private = private.decrypt(rsa_public)

        println(rsa_private.decodeToString())

        val serverSocket = aSocket(selectorManager).tcp().bind(port = defaultPort)

        println("Server started at port $defaultPort")

        while(true) {
            val socket = serverSocket.accept()
            println("Accepted $socket")
        }
    }
}