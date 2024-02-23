package nyc.spookyrobotics.piserver.tcpServer.zipServer

import nyc.spookyrobotics.piserver.tcpServer.handlers.ContextHandler
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.io.File

class FileHandler(val parentContext: String, val source: File) : ContextHandler() {
    init {
        if (!source.exists() || !source.isFile) {
            throw IllegalStateException("File Handler must instantiate with valid file")
        }
    }
    override fun getContext(): String = parentContext

    override fun getHandler(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            sendFile(exchange, source.absolutePath)
        }
    }
}