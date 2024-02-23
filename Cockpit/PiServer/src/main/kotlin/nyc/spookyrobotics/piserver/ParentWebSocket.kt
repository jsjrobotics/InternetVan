package nyc.spookyrobotics.piserver

import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoWSD
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.IOException

open class ParentWebSocket(request: IHTTPSession, private val websocketServer: WebsocketServer) : NanoWSD.WebSocket(request) {

    private val _messageSubject = PublishSubject.create<String>()
    val id = request.hashCode()
    val messageSubject: Observable<String> = _messageSubject

    override fun onOpen() {
        println("socket opened")
        Thread {
            while(true) {
                Thread.sleep(1000)
                ping("0".toByteArray())
            }
        }.start()
    }

    override fun onClose(
        code: NanoWSD.WebSocketFrame.CloseCode?,
        reason: String?,
        initiatedByRemote: Boolean
    ) {
        println("socket closed $id")
        websocketServer.socketClosed(id)
    }

    override fun onMessage(message: NanoWSD.WebSocketFrame) {
        println("received message: ${message.textPayload}")
        _messageSubject.onNext(message.textPayload)
    }

    override fun onPong(pong: NanoWSD.WebSocketFrame?) {
        //println("pong received")
    }

    override fun onException(exception: IOException) {
        println("exception triggered: ${exception.message}")
        exception.printStackTrace()
    }
}