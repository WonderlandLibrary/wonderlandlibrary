package me.offeex.bloomware.api.config

import com.google.gson.JsonElement
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.manager.managers.FileManager.loadFileContent
import me.offeex.bloomware.api.util.SerializationUtil

abstract class Config {
    abstract val filePath: FilePath

    fun loadExternal() {
        val type = filePath.name

        val content = filePath.file.loadFileContent()
            ?: return Bloomware.LOGGER.error("$type file does not exist!")
        if (content.isEmpty()) {
            return Bloomware.LOGGER.error("$type file is empty!")
        }
        val jsonObject = SerializationUtil.deserialize(content.joinToString("\n"))
            ?: return Bloomware.LOGGER.error("Deserialization error in $type:\n$content")

        try {
            load(jsonObject)
        } catch (e: Exception) {
            Bloomware.LOGGER.error("$type file is corrupted! Loading default values...")
            e.printStackTrace()
        }
    }

    fun saveExternal() {
        val data = save()
        if (data != null) filePath.file.printWriter().use {
            it.write(SerializationUtil.serialize(data))
        }
    }

    protected inline fun <R> tryParse(handle: String, callback: () -> R) = try {
        callback()
    } catch (e: Exception) {
        Bloomware.LOGGER.error(handle)
        throw e
    }

    protected abstract fun load(element: JsonElement)
    protected abstract fun save(): Any?
}