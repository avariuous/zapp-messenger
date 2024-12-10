package ru.sliva.zapp.data.utils

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Buffer.readString() : String {
    val length = readInt()
    val bytes = readByteArray(length)
    return String(bytes)
}

fun Buffer.writeString(string: String) = string.encodeToByteArray().let {
    writeInt(it.size)
    write(it)
}

inline fun <reified T> Buffer.read() = Json.decodeFromString<T>(readString())

inline fun <reified T> Buffer.write(obj: T) = writeString(Json.encodeToString(obj))