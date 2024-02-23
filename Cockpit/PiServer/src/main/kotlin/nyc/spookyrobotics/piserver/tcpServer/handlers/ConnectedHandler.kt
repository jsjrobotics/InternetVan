package nyc.spookyrobotics.piserver.tcpServer.handlers


import com.sun.net.httpserver.HttpHandler
import nyc.spookyrobotics.piserver.tcpServer.TcpServerConstants

class ConnectedHandler() : ContextHandler() {
    private val response: String = buildJsonResponse()
    override fun getContext(): String {
        return TcpServerConstants.urlRoot
    }

    override fun getHandler(): HttpHandler {
        return HttpHandler {  httpExchange ->
            sendOk(httpExchange, response)
        }
    }

    private fun buildJsonResponse() : String {
        return "Connected"
    }

}