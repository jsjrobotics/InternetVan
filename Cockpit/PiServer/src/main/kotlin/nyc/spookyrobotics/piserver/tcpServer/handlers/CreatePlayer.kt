package nyc.spookyrobotics.piserver.tcpServer.handlers


import nyc.spookyrobotics.piserver.database.DatabaseFunctions
import com.google.gson.Gson
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import nyc.spookyrobotics.piserver.tcpServer.TcpServerConstants

class CreatePlayer(private val databaseFunctions: DatabaseFunctions) : ContextHandler() {
    private val gson = Gson()

    override fun getContext(): String {
        return TcpServerConstants.urlCreatePlayer
    }

    override fun getHandler(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            if (!verifyRequestMethod(exchange, TcpServerConstants.CreatePlayer.method)) {
                return@HttpHandler
            }
            val requestMap = buildQueryMap(exchange)
            val response = buildJsonResponse(requestMap)
            if (response == null) {
                sendInvalid(exchange)
            } else {
                sendOk(exchange, response)
            }
        }
    }

    private fun buildJsonResponse(requestMap: Map<String, String>): String? {
        val username = requestMap["username"] ?: return null
        val email = requestMap["email"] ?: return null
        val password = requestMap["password"] ?: return null

        val resultSet = databaseFunctions.createPlayer(username, email, password)
        return gson.toJson(PlayerId(resultSet))
    }

}
