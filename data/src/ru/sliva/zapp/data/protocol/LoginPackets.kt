package ru.sliva.zapp.data.protocol

import korlibs.encoding.base64
import korlibs.encoding.fromBase64
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import ru.sliva.zapp.data.BotCredentials
import ru.sliva.zapp.data.Credentials
import ru.sliva.zapp.data.UserCredentials
import ru.sliva.zapp.data.UserType
import ru.sliva.zapp.data.UserType.Companion.toUserType
import ru.sliva.zapp.data.utils.RSA.toPublicKey
import ru.sliva.zapp.data.utils.readString
import ru.sliva.zapp.data.utils.writeString
import java.security.PublicKey

// Serverbound packets

data class HandshakeC2S (
    var rsaKey: PublicKey
) : Packet {

    override val packetId: Byte = 0x00

    override fun write(buffer: Buffer) {
        buffer.write(rsaKey.encoded)
    }

    override fun read(buffer: Buffer) {
        rsaKey = buffer.readByteArray(294).toPublicKey()
    }
}

data class LoginC2S (
    var userType: UserType,
    var credentials: Credentials
) : Packet {

    override val packetId: Byte = 0x01

    override fun read(buffer: Buffer) {
        userType = buffer.readByte().toUserType() ?: error("Invalid user type")
        credentials = when (userType) {
            UserType.USER -> UserCredentials(buffer.readString(), buffer.readString())
            UserType.BOT -> BotCredentials(buffer.readString())
        }
    }

    override fun write(buffer: Buffer) {
        buffer.writeByte(userType.id)
        when (credentials) {
            is UserCredentials -> {
                val userCredentials = credentials as UserCredentials
                buffer.writeString(userCredentials.login)
                buffer.writeString(userCredentials.password)
            }
            is BotCredentials -> {
                val botCredentials = credentials as BotCredentials
                buffer.writeString(botCredentials.token)
            }
        }
    }
}

// Clientbound packets
data class HandshakeS2C (
    var aesKey: String
) : Packet {

    override val packetId: Byte = 0x00

    override fun write(buffer: Buffer) {
        buffer.write(aesKey.fromBase64())
    }

    override fun read(buffer: Buffer) {
        aesKey = buffer.readByteArray(32).base64
    }
}