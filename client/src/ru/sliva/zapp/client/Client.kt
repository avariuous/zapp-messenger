package ru.sliva.zapp.client

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import korlibs.encoding.fromBase64
import korlibs.io.compression.compress
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.Buffer
import ru.sliva.zapp.data.UserCredentials
import ru.sliva.zapp.data.UserType
import ru.sliva.zapp.data.protocol.*
import ru.sliva.zapp.data.utils.AES.decryptAes
import ru.sliva.zapp.data.utils.AES.encryptAes
import ru.sliva.zapp.data.utils.RSA
import ru.sliva.zapp.data.utils.RSA.decrypt
import ru.sliva.zapp.data.utils.copyOrNull

object Client {

    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private val defaultPort = 9002
    lateinit var socket: Socket

    var rsaKeyPair = RSA.generateKeyPair()
    var aesKey: ByteArray? = null

    private var activeReadChannel: ByteReadChannel? = null

    val readChannel: ByteReadChannel
        get() = activeReadChannel ?: socket.openReadChannel().also { activeReadChannel = it }

    private var activeWriteChannel: ByteWriteChannel? = null

    val writeChannel: ByteWriteChannel
        get() = activeWriteChannel ?: socket.openWriteChannel(autoFlush = true).also { activeWriteChannel = it }

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        socket = aSocket(selectorManager).tcp().connect("127.0.0.1", port = defaultPort)

        sendPacket(HandshakeC2S(rsaKeyPair.public))

        while(true) {
            readPacket()?.let { handlePacket(it) } ?: println("Null packet")
        }
    }

    suspend fun handlePacket(packet: ClientBoundPacket) = when(packet) {
        is HandshakeS2C -> {
            println("Received AES key: ${packet.aesKey}")

            aesKey = packet.aesKey.fromBase64()

            sendPacket(LoginC2S(
                userType = UserType.USER,
                credentials = UserCredentials("login", "password")
            ))
        }
        is SuccessLogin -> {
            println("Successfully logged in")
            println(packet.entity)
        }
    }

    suspend fun sendPacket(packet: ServerBoundPacket) {
        val buffer = Buffer()

        packet.write(buffer)

        var bytes = buffer.readBytes().compress(GZIP)

        if(aesKey != null) {
            bytes = bytes.encryptAes(aesKey!!)
        }

        writeChannel.writeByte(packet.packetId)
        writeChannel.writeInt(bytes.size)
        writeChannel.writeFully(bytes)
    }

    suspend fun readPacket() : ClientBoundPacket? {
        val packetId = readChannel.readByte()
        val dataLength = readChannel.readInt()

        var bytes = readChannel.readByteArray(dataLength)

        bytes = if(aesKey != null) {
            bytes.decryptAes(aesKey!!)
        } else {
            rsaKeyPair.private.decrypt(bytes)
        }

        bytes = bytes.uncompress(GZIP)

        val buffer = Buffer()

        buffer.write(bytes)

        return clientBoundPackets
            .firstOrNull { it.packetId == packetId }
            ?.copyOrNull()
            ?.apply {
                read(buffer)
            }
    }
}