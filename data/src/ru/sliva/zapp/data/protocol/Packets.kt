package ru.sliva.zapp.data.protocol

import kotlinx.io.Buffer

sealed interface Packet {

    val packetId: Byte

    fun write(buffer: Buffer)

    fun read(buffer: Buffer)

}