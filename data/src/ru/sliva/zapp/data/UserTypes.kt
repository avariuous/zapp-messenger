package ru.sliva.zapp.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface Entity {
    val id: Int
    val name: String
    val username: String?
    val description: String?
}

@Serializable
data class User(
    override val id: Int,
    override val name: String,
    override val username: String?,
    override val description: String?,
    val lastSeen: Long?
) : Entity

@Serializable
data class Bot(
    override val id: Int,
    override val name: String,
    override val username: String?,
    override val description: String?,
    val commands: List<String>
) : Entity
