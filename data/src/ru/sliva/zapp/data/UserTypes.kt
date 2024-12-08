package ru.sliva.zapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class Entity {
    abstract val id: Int
    abstract val type: UserType
    abstract val name: String
    abstract val username: String?
    abstract val description: String?
}

@Serializable
@SerialName("user")
data class User(
    override val id: Int,
    override val type: UserType,
    override val name: String,
    override val username: String?,
    override val description: String?,
    val lastSeen: Long?
) : Entity()

@Serializable
@SerialName("bot")
data class Bot(
    override val id: Int,
    override val type: UserType,
    override val name: String,
    override val username: String?,
    override val description: String?,
    val commands: List<String>
) : Entity()

@Serializable
enum class UserType(val id: Byte) {
    @SerialName("user")
    USER(0x00),
    @SerialName("bot")
    BOT(0x01)
}
