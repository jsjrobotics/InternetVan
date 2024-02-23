package nyc.spookyrobotics.piserver

import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoWSD

class GraphPaperSocket(
    request: IHTTPSession, webSocketServer: WebsocketServer,
    private val graphPaperStateManager: GraphPaperStateManager
) : ParentWebSocket(request, webSocketServer ) {

    override fun onOpen() {
        super.onOpen()
        val currentState = graphPaperStateManager.currentState
        if (currentState != null) {
            send(currentState)
        }
    }

    override fun onMessage(message: NanoWSD.WebSocketFrame) {
        graphPaperStateManager.updateState(message.textPayload)
        super.onMessage(message)
    }
}