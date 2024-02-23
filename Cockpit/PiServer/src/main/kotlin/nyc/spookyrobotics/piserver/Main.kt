package nyc.spookyrobotics.piserver

import nyc.spookyrobotics.piserver.tcpServer.ServerId
import nyc.spookyrobotics.piserver.tcpServer.TopLevelServer
import java.io.File

fun main(args: Array<String>) {
    Class.forName("com.mysql.cj.jdbc.Driver");
    if (args.size != 1) {
        printErrorAndExit()
    }
    val initFile = File(args[0])
    if (!initFile.exists()) {
        printErrorAndExit()
    }
    val serverId = loadServerFile(initFile)
    val server: TopLevelServer = TopLevelServer(serverId)
    server.start()
    val websocketServer = WebsocketServer(serverId)
    websocketServer.start()
}

fun printErrorAndExit() {
    println("Expected path to init file with following structure")
    println("1st Line: Database connection url")
    println("2nd Line: Database connection username")
    println("3rd Line: Database connection password")
    println("4th Line: Integer defining http port number")
    println("5th Line: File path for the keystore file")
    println("6th Line: Keystore file password")
    println("7th Line: Integer defining websocket server port number")
    throw IllegalArgumentException("Expected path to init file.")
}

fun loadServerFile(serverFile: File): ServerId {
    if (serverFile.exists()) {
        val lines = serverFile.readLines()
        if (lines.size != 7) {
            throw IllegalArgumentException("init file does not follow expected format")
        }
        val databaseConnection: String = lines[0]
        val user : String = lines[1]
        val password : String = lines[2]
        val httpPort: Int = lines[3].toInt()
        val keystoreFile: File = File(lines[4])
        val keystorePassword: String = lines[5]
        val websocketPort: Int = lines[6].toInt()
        val result = ServerId (
            httpPort,
            websocketPort,
            databaseConnection,
            user,
            password,
            keystoreFile,
            keystorePassword
        )
        println("Loaded config $result")
        return result
    }
    throw IllegalArgumentException("init file not found")
}