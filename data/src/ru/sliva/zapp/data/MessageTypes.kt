package ru.sliva.zapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val sender: Int,
    val chat: Int,
    val time: Long,
    val content: String,
    val attachments: List<Attachment>
)

@Serializable
sealed interface Attachment

@Serializable
@SerialName("file")
open class File(
    val size: Byte
) : Attachment