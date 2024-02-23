package nyc.spookyrobotics.piserver.tcpServer.zipServer

import nyc.spookyrobotics.piserver.tcpServer.ServerId
import nyc.spookyrobotics.piserver.tcpServer.TcpServer
import nyc.spookyrobotics.piserver.tcpServer.handlers.ContextHandler
import java.io.File
import java.sql.DriverManager

class ZipServer(id: ServerId, zipFile: File, manifest: File) : TcpServer(id, getHandlers(id, zipFile, manifest), id.keystoreFile, id.keystorePassword.toCharArray()) {

    companion object {
        private fun getHandlers(id: ServerId, zipFile: File, manifest: File): List<ContextHandler> {
            val databaseConnection = DriverManager.getConnection(id.databaseUrl, id.user, id.password)
            return arrayListOf<ContextHandler>(
                FileHandler("/resources", zipFile),
                FileHandler("/manifest", manifest)
            )
        }
    }

}