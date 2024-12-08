package ru.sliva.zapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entity(
    val id: Int,
    val type: UserType,
    val name: String,
    val username: String?,
    val description: String?,
    val properties: Properties
)

@Serializable
sealed interface Properties

@Serializable
@SerialName("user")
data class UserProperties(
    val lastSeen: Long?
) : Properties

@Serializable
@SerialName("bot")
data class BotProperties(
    val commands: List<String>
) : Properties

@Serializable
enum class UserType(val id: Byte) {
    @SerialName("user")
    USER(0x00),
    @SerialName("bot")
    BOT(0x01)
}
