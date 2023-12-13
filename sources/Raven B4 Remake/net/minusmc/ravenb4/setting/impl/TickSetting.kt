package net.minusmc.ravenb4.setting.impl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minusmc.ravenb4.setting.Setting

class TickSetting(name: String, value: Boolean): Setting<Boolean>(name, value) {
    override val settingType = "tick"

    override fun getConfigAsJson(): JsonElement {
        val data = JsonObject()
        data.addProperty("type", settingType)
        data.addProperty("value", value)
        return data
    }

    override fun applyConfigFromJson(data: JsonObject) {
        if (!data.get("type").asString.equals(settingType, true)) return;
        set(data.get("value").asBoolean)
    }

    fun toggle() {value = !value}
    fun enable() {value = true}
    fun disable() {value = false}
}