package net.minusmc.ravenb4.setting.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minusmc.ravenb4.setting.Setting

class ComboSetting(name: String, val modes: Array<String>, value: String): Setting<String>(name, value) {
	override val settingType = "mode"

	override fun getConfigAsJson(): JsonElement {
        val data = JsonObject()
        data.addProperty("type", settingType)
        data.addProperty("value", value)
        return data
    }

    override fun applyConfigFromJson(data: JsonObject) {
        if (!data.get("type").asString.equals(settingType, true)) return;
        
        value = data.get("value").asString
        for (mode in modes) {
            if (mode.equals(value)) set(value)
        }
    }

    fun nextMode() {
        val currentIdx = modes.indexOf(value)
        if (currentIdx != -1) value = modes[(currentIdx + 1) % modes.size]
    }
}