package de.tuberlin.cnae.collector

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

@Service
class WriteService(
    private val template: MongoTemplate,
    private val mapper: ObjectMapper,
) {

    fun write(json: String) {
        try {
            save(json)
        } catch (e: Exception) {
            System.err.println("Error parsing JSON: " + e.message)
        }
    }

    @Throws(IOException::class)
    private fun save(json: String) {
        val original = mapper.readTree(json)
        if (!original.has("request") && !original.has("response")) {
            val id = original["id"].asText()
            println("Ignore message (missing request or response): $id")
            return
        }

        val root = mapper.createObjectNode()
        val list: List<String> = mutableListOf("request", "response", "id", "startTime")
        for (key in list) {
            root.set<JsonNode>(key, original[key])
        }

        println("Received message size: " + json.length)

        template.getCollection("traffic").insertOne(
            Document.parse(root.toString())
        )
    }

}