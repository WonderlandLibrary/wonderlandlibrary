package me.offeex.bloomware.api.config.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.config.Config
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.manager.managers.CommandManager

object PrefixConfig : Config() {
    override val filePath = FilePath.PREFIX

    override fun save() = JsonObject().apply {
        this.addProperty("prefix", CommandManager.prefix)
    }

    override fun load(element: JsonElement) {
        val jsonObject = element.asJsonObject
        val prefix = jsonObject.get("prefix").asString

        if (prefix.length == 1) CommandManager.prefix = prefix
        else Bloomware.LOGGER.error("Invalid command prefix \"$prefix\"! Loading default one...")
    }
}