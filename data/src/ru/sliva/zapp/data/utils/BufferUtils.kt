package ru.sliva.zapp.data.utils

import kotlinx.io.Buffer
import kotlinx.io.readByteArray

fun Buffer.readString() : String {
    val length = readInt()
    val bytes = readByteArray(length)
    return String(bytes)
}

fun Buffer.writeString(string: String) {
    writeInt(string.length)
    write(string.encodeToByteArray())
}