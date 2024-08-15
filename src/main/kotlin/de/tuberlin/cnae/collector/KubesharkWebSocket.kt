package de.tuberlin.cnae.collector

import de.tuberlin.cnae.collector.common.extensions.logger
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI
import java.util.concurrent.CompletableFuture

@Service
class KubesharkWebSocket(
    private val webSocketClient: WebSocketClient,
    private val writeService: WriteService,
    private val config: KubesharkWebSocketConfig,
) : TextWebSocketHandler() {

    private val logger = logger<KubesharkWebSocket>()

    fun connect() {
        logger.info("Connecting to Kubeshark to ${config.host}")
        val uri = "ws://${config.host}/api/wsFull"

        @Suppress("DEPRECATION")
        webSocketClient.doHandshake(this, uri).addCallback(
            { logger.info("Connection established") },
            { ex: Throwable -> logger.error("Failed to connect: ${ex.message}") }
        )
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connection established: $session")
        session.sendMessage(TextMessage("""
            (src.namespace == "otel-demo" or dst.namespace == "otel-demo") and http
        """.trimIndent()))
        session.attributes["json"] = StringBuilder(session.textMessageSizeLimit)
    }

    @Throws(Exception::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message size: ${message.payload.length}")
        val builder = session.attributes["json"] as StringBuilder?

        builder!!.append(message.payload)
        if (message.isLast) {
            writeService.write(builder.toString())
            builder.setLength(0)
        }
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Transport error: ${exception.message}")
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("Connection closed: $session status: $status")
    }

    override fun supportsPartialMessages(): Boolean = true
}