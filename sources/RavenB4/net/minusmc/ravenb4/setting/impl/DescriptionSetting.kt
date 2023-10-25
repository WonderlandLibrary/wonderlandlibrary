package net.minusmc.ravenb4.setting.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minusmc.ravenb4.setting.Setting

class DescriptionSetting(name: String, value: String): Setting<String>(name, value) {
	override val settingType = "desc"

	override fun getConfigAsJson(): JsonElement {
        val data = JsonObject()
        data.addProperty("type", settingType)
        data.addProperty("value", value)
        return data
    }

    override fun applyConfigFromJson(data: JsonObject) {
        if (!data.get("type").asString.equals(settingType, true)) return;
        set(data.get("value").asString)
    }
}