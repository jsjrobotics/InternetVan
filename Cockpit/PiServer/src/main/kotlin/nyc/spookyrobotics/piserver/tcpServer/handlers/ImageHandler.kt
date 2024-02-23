package nyc.spookyrobotics.piserver.tcpServer.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import nyc.spookyrobotics.piserver.tcpServer.TcpServerConstants
import java.io.File

class ImageHandler(val resourcesDirectory: File) : ContextHandler() {
    val DEFAULT_IMAGE = "DEFAULT_IMAGE.png"

    private val defaultImage = File(resourcesDirectory, DEFAULT_IMAGE)
    override fun getContext(): String {
        return TcpServerConstants.urlGetImage
    }

    override fun getHandler(): HttpHandler {
        return HttpHandler { exchange: HttpExchange ->
            if (!verifyRequestMethod(exchange, TcpServerConstants.HTTP_GET)) {
                return@HttpHandler
            }
            val responsePath = buildResponsePath(exchange)
            sendFile(exchange, responsePath)
        }
    }

    private fun buildResponsePath(exchange: HttpExchange): String {
        val path = exchange.requestURI.toString().substringAfterLast("/")
        val imageSource = File(resourcesDirectory, path)
        if (imageSource.exists()) {
            return imageSource.absolutePath
        }
        return defaultImage.absolutePath
    }
}