package ru.sliva.zapp.data.protocol

import kotlinx.io.Buffer

val serverBoundPackets = listOf(
    HandshakeC2S(),
    LoginC2S()
)

val clientBoundPackets = listOf(
    HandshakeS2C(),
    SuccessLogin()
)

sealed interface Packet {

    val packetId: Byte

    fun write(buffer: Buffer)

    fun read(buffer: Buffer)

}

sealed interface ClientBoundPacket : Packet

sealed interface ServerBoundPacket : Packet