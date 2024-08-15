package de.tuberlin.cnae.collector

import jakarta.websocket.ContainerProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient

@Configuration
class WebSocketConfig {
    @Bean
    fun webSocketClient(): WebSocketClient {
        val client = StandardWebSocketClient()
        val container = ContainerProvider.getWebSocketContainer()
        container.defaultMaxTextMessageBufferSize = 65536
        container.defaultMaxBinaryMessageBufferSize = 65536
        return client
    }
}

@ConfigurationProperties(prefix = "kubeshark.websocket")
data class KubesharkWebSocketConfig(
    val host: String,
)