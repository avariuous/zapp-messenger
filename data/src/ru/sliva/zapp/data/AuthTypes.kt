package ru.sliva.zapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserType {
    @SerialName("user")
    USER,

    @SerialName("bot")
    BOT
}

@Serializable
sealed interface Credentials

@Serializable
@SerialName("user")
class UserCredentials(
    val login: String,
    val password: String
) : Credentials

@Serializable
@SerialName("bot")
class BotCredentials(
    val token: String
) : Credentials