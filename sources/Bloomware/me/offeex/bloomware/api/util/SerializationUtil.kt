package me.offeex.bloomware.api.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.offeex.bloomware.Bloomware

object SerializationUtil {
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    fun serialize(obj: Any): String {
        return GSON.toJson(obj)
    }

    fun deserialize(content: String): JsonElement? {
        return try {
            JsonParser.parseString(content)
        } catch (e: Exception) {
            Bloomware.LOGGER.error("Error while deserializing:\n$content")
            e.printStackTrace()
            null
        }
    }
}