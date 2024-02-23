package nyc.spookyrobotics.piserver

import nyc.spookyrobotics.piserver.tcpServer.ServerId
import fi.iki.elonen.NanoWSD

class WebsocketServer(private val serverId: ServerId): NanoWSD(serverId.websocketPort) {
    private val socketConnections = mutableListOf<nyc.spookyrobotics.piserver.ParentWebSocket>()
    private var graphPaperStateManager = GraphPaperStateManager()
    override fun openWebSocket(handshake: IHTTPSession): WebSocket {
        val uri = handshake.uri
        println("Connection ${handshake.hashCode()} made to ${handshake.remoteHostName} $uri ${handshake.remoteIpAddress}")
        return initSocket(handshake)
    }

    override fun start() {
        println("Starting websocket server")
        println("ws://localhost:${serverId.websocketPort}/")
        println("-------------------------------------------------")
        super.start()
    }

    private val headerSocketMap = mapOf<String, (IHTTPSession) -> nyc.spookyrobotics.piserver.ParentWebSocket>(
        Pair("ChillQueens") { GraphPaperSocket(it, this, graphPaperStateManager) }
    )

    private fun initSocket(handshake: IHTTPSession): WebSocket {
        val HEADER_CHANNELS: String = "channels"
        val channels = handshake.headers[HEADER_CHANNELS]
        val isGraphPaperSocket = channels?.contains("GraphPaper") ?: false
        val socket = if (isGraphPaperSocket) GraphPaperSocket(handshake, this, graphPaperStateManager) else nyc.spookyrobotics.piserver.ParentWebSocket(
            handshake,
            this
        )
        socketConnections.add(socket)
        val filteringEnabled = !isGraphPaperSocket
        socket.messageSubject.subscribe (
            {message ->
                handleMessageReceived(socket.id, message, filteringEnabled)
            },
            {
                println("Error: ${it.message}")
                it.printStackTrace()
            }
        )
        return socket
    }

    private fun handleMessageReceived(id: Int, message: String, filterEnabled: Boolean = true) {
        val deadSockets = mutableListOf<nyc.spookyrobotics.piserver.ParentWebSocket>()
        val filteredConnections = if (filterEnabled) socketConnections.filterNot { it.id == id } else socketConnections
        filteredConnections
            .forEach { webSocket ->
                if (!webSocket.isOpen) {
                    deadSockets.add(webSocket)
                    return@forEach
                }
                println("Forwarding message to ${webSocket.id}")
                Thread{ webSocket.send(message) }.start()
            }
        socketConnections.removeAll(deadSockets)
    }

    fun socketClosed(id: Int) {
        socketConnections.removeIf { it.id  == id}
        if(!socketConnections.any{ it is GraphPaperSocket}) {
            graphPaperStateManager.reset()
        }

    }
}