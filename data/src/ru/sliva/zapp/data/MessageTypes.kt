package ru.sliva.zapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ChatEvent {
    abstract val id: Int
    abstract val sender: Int
    abstract val chat: Int
    abstract val time: Long
}

@Serializable
@SerialName("chatjoin")
class ChatJoin(
    override val id: Int,
    override val sender: Int,
    override val chat: Int,
    override val time: Long
) : ChatEvent()

@Serializable
@SerialName("chatleave")
class ChatLeave(
    override val id: Int,
    override val sender: Int,
    override val chat: Int,
    override val time: Long
) : ChatEvent()

@Serializable
@SerialName("message")
class Message(
    override val id: Int,
    override val sender: Int,
    override val chat: Int,
    override val time: Long,
    val content: String,
    val attachments: List<Attachment>
) : ChatEvent()

@Serializable
sealed interface Attachment

@Serializable
@SerialName("file")
open class File(
    val size: Byte
) : Attachment