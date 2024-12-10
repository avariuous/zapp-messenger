package ru.sliva.zapp.server

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object Server {

    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private val defaultPort = 9002

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val serverSocket = aSocket(selectorManager).tcp().bind(port = defaultPort)

        println("Server started at port $defaultPort")

        while(true) {
            ServerSession(serverSocket.accept())
        }
    }
}