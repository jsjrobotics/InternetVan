package nyc.spookyrobotics.piserver.tcpServer.handlers

import nyc.spookyrobotics.piserver.database.DatabaseFunctions
import com.google.gson.Gson
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import nyc.spookyrobotics.piserver.tcpServer.TcpServerConstants

class GetApks(private val databaseFunctions: DatabaseFunctions) : ContextHandler() {
    private val gson = Gson()

    override fun getContext(): String {
        return TcpServerConstants.urlGetApks
    }

    override fun getHandler(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            if (!verifyRequestMethod(exchange, TcpServerConstants.GetApks.method)) {
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
        val resultSet = databaseFunctions.getApks()
        return gson.toJson(resultSet)
    }

}
