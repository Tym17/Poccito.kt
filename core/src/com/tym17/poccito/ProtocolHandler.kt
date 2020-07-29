package com.tym17.poccito

import ktx.log.logger
import java.net.Socket
import java.util.*
import com.badlogic.gdx.utils.Array
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.*

private val log = logger<ProtocolHandler>()

class ProtocolHandler(ip: String, port: Int, _name: String) {
    val P_UP = "UP"
    val P_DOWN = "DOWN"
    val P_LEFT = "LEFT"
    val P_RIGHT = "RIGHT"
    val P_MAX_QUEUE = 1

    var clientSocket = Socket(ip, port)
    var myId = -1
    val commands = Array<String>()
    val name = _name
    val mutex = Mutex()

    init {
        if (clientSocket.isConnected) {
            log.debug { "Connected to $ip:$port." }
        } else {
            throw Error("Could not connect")
        }
        clientSocket.outputStream.write("HI $name\u0000".toByteArray())
    }
    // https://stackoverflow.com/questions/56535473/how-to-send-and-receive-strings-through-tcp-connection-using-kotlin
    var scanner = Scanner(clientSocket.inputStream)

    fun move(where: String) {
        clientSocket.outputStream.write("MV $where\u0000".toByteArray())
    }

    fun receive() {
        GlobalScope.launch {
            if (scanner.hasNextLine()) {
                var cmd = scanner.nextLine()
                log.debug { "Received '$cmd'" }
                if (myId == -1) {
                    val detailedCmd = cmd.split(' ')
                    if (detailedCmd[0] == "HELLO") {
                        mutex.withLock {
                            myId = detailedCmd[1].toInt()
                        }
                        log.debug { "My id is now $myId" }
                        cmd = "NEW $myId ${detailedCmd[2]} ${detailedCmd[3]} $name"

                    }
                }
                mutex.withLock {
                    commands.add(cmd)
                }
            }
        }
    }

    fun haveCmds(): Boolean {
        var ret = false
        runBlocking {
            if (mutex.tryLock()) {
                ret = !commands.isEmpty
                mutex.unlock()
            }
        }
        return ret
    }

    fun extract(): String {
        var ret = "NULL"
        runBlocking {
            if (mutex.tryLock()) {
                ret = commands.pop()
                mutex.unlock()
            }
        }
        return ret
    }

    fun disconnect() {
        clientSocket.outputStream.write("QUIT $myId\u0000".toByteArray())
        clientSocket.close()
    }
}