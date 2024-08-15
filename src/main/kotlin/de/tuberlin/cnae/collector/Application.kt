package de.tuberlin.cnae.collector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(KubesharkWebSocketConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
