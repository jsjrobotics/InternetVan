package nyc.spookyrobotics.piserver.tcpServer

import nyc.spookyrobotics.piserver.database.DatabaseFunctions
import nyc.spookyrobotics.piserver.database.DatabaseQueries
import nyc.spookyrobotics.piserver.database.StatementBuilder
import nyc.spookyrobotics.piserver.tcpServer.handlers.*
import java.sql.Connection
import java.sql.DriverManager

class TopLevelServer(id: ServerId) : TcpServer(id, getHandlers(id), id.keystoreFile, id.keystorePassword.toCharArray()) {

    companion object {
        private val queryProvider = DatabaseQueries()
        private var databaseConnection: Connection? = null

        private fun getHandlers(id: ServerId): List<ContextHandler> {
            val statementBuilderProvider = {
                getStatementBuilder(id)
            }
            val databaseFunctions = DatabaseFunctions(statementBuilderProvider)
            val handlers : ArrayList<ContextHandler> = arrayListOf(
                ConnectedHandler(),
                CreatePlayer(databaseFunctions),
                GetApks(databaseFunctions),
            )
            return handlers
        }

        private fun getStatementBuilder(id: ServerId): StatementBuilder? {
            initConnectionIfNecessary(id)
            return databaseConnection?.let { connection: Connection ->
                StatementBuilder(connection, queryProvider)
            } ?: run {
                System.err.println("Unable to get database connection")
                null
            }
        }

        private fun initConnectionIfNecessary(id: ServerId) {
            try {
                val timeout = 5
                if (databaseConnection?.isValid(timeout) != true) {
                    databaseConnection = null
                }
            } catch (e: Exception) {
                databaseConnection = null
            }
            if (databaseConnection == null) {
                databaseConnection = DriverManager.getConnection(id.databaseUrl, id.user, id.password)
            }
        }
    }

}