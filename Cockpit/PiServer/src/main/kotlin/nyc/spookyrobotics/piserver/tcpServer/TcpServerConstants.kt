package nyc.spookyrobotics.piserver.tcpServer

object TcpServerConstants {
    object CreatePlayer: HttpEndpont {
        override val method = HTTP_POST

    }

    object GetApks: HttpEndpont {
        override val method = HTTP_GET
    }

    val HTTP_POST = "POST"
    val HTTP_GET: String = "GET"
    val urlGetImage: String = "/image"
    val urlCreatePlayer: String = "/createPlayer"
    val urlGetApks: String = "/apks"
    val urlRoot: String = "/"
}
