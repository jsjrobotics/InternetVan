package nyc.spookyrobotics.piserver.tcpServer

import nyc.spookyrobotics.piserver.tcpServer.handlers.ContextHandler
import com.sun.net.httpserver.HttpServer
import com.sun.net.httpserver.HttpsConfigurator
import com.sun.net.httpserver.HttpsParameters
import com.sun.net.httpserver.HttpsServer
import java.io.File
import java.io.FileInputStream
import java.net.InetSocketAddress
import java.security.KeyStore
import java.util.concurrent.Executors
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

abstract class TcpServer (
    protected val id: ServerId,
    protected val handlers: List<ContextHandler>,
    protected val keystoreFile: File,
    protected val keystorePassword: CharArray?,
    protected val sslProtocol:String = "TLS"
){
    private val sslContext: SSLContext = buildSslContext(keystoreFile, keystorePassword, sslProtocol)
    private val httpServer: HttpServer = buildServer()

    fun buildSslServer(): HttpsServer {
        val server = HttpsServer.create(InetSocketAddress(id.httpPort), BACKLOG_QUEUE)
        server.httpsConfigurator = buildHttpsConfigurator()
        server.executor = Executors.newCachedThreadPool()
        for (handler in handlers) {
            server.createContext(handler.getContext(), handler.getLoggingHandler())
        }
        return server
    }

    fun buildServer(): HttpServer {
        val server = HttpServer.create(InetSocketAddress(id.httpPort), BACKLOG_QUEUE)
        server.executor = Executors.newCachedThreadPool()
        for (handler in handlers) {
            server.createContext(handler.getContext(), handler.getLoggingHandler())
        }
        return server
    }

    private fun buildHttpsConfigurator(): HttpsConfigurator {
        return object : HttpsConfigurator(sslContext) {
            override fun configure(params: HttpsParameters) {
                try {
                    super.configure(params)
                    val engine = sslContext.createSSLEngine()
                    params.needClientAuth = false
                    params.cipherSuites = engine.enabledCipherSuites
                    params.protocols = engine.enabledProtocols
                    params.setSSLParameters(sslContext.supportedSSLParameters)
                } catch (e: Exception) {
                    System.err.println("Failed to create https connection: $e")
                }

            }
        }
    }
    private fun buildSslContext(keystoreFile: File, keystorePassword: CharArray?, protocol: String): SSLContext {
        val keystore = KeyStore.getInstance("PKCS12")
        keystore.load(FileInputStream(keystoreFile), keystorePassword)
        val keyMgrFactory = KeyManagerFactory.getInstance("SunX509")
        val trustManagerAlgorithm = "SunX509"
        val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)

        keyMgrFactory.init(keystore, keystorePassword)
        trustManagerFactory.init(keystore)
        val sslContext = SSLContext.getInstance(protocol)
        sslContext.init(keyMgrFactory.keyManagers,
            trustManagerFactory.trustManagers,
            null)
        return sslContext
    }

    fun start() {
        println("Starting http server")
        println("http://localhost:${id.httpPort}/")
        println("-------------------------------------------------")
        httpServer.start()
    }

    private val name: String
        get() = javaClass.name

    fun stop() {
        httpServer.stop(SHUDOWN_TASK_COMPLETION_DELAY_SECONDS)
    }

    companion object {
        private val BACKLOG_QUEUE = 2
        private val SHUDOWN_TASK_COMPLETION_DELAY_SECONDS = 0
    }
}