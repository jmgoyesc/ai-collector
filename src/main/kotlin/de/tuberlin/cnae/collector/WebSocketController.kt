package de.tuberlin.cnae.collector

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/websocket", produces = ["application/json"])
class WebSocketController(private val websocket: KubesharkWebSocket) {

    @PostMapping
    fun connect() = websocket.connect()

}
