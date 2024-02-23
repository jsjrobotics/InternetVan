package nyc.spookyrobotics.piserver.tcpServer

import java.io.File

data class ServerId(
    val httpPort: Int,
    val websocketPort: Int,
    val databaseUrl: String,
    val user: String,
    val password: String,
    val keystoreFile: File,
    val keystorePassword: String,
) {

}
