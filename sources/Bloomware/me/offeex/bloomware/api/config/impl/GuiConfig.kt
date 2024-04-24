package me.offeex.bloomware.api.config.impl

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.config.Config
import me.offeex.bloomware.api.helper.FilePath

object GuiConfig : Config() {
    override val filePath = FilePath.GUI

    override fun save() = JsonArray().apply {
        Bloomware.gui.frames.forEach { frame ->
            val obj = JsonObject()
            obj.addProperty("category", frame.category.title)
            obj.addProperty("x", frame.x)
            obj.addProperty("y", frame.y)
            this.add(obj)
        }
    }

    override fun load(element: JsonElement) {
        for (entry in element.asJsonArray) {
            val frameObject = entry.asJsonObject

            val categoryName = try {
                frameObject.get("category").asString
            } catch (e: Exception) {
                Bloomware.LOGGER.error("Error while loading GUI category: $frameObject}")
                e.printStackTrace()
                return
            }

            val frame = Bloomware.gui.getFrameByCategory(categoryName) ?: continue

            try {
                frame.x = frameObject.get("x").asInt
                frame.y = frameObject.get("y").asInt
            } catch (e: Exception) {
                Bloomware.LOGGER.error("Gui coordinate is corrupted: $frameObject")
                e.printStackTrace()
                return
            }
        }
    }

}