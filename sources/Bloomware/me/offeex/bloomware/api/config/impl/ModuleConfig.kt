package me.offeex.bloomware.api.config.impl

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.config.Config
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.manager.managers.ModuleManager
import me.offeex.bloomware.client.module.HudModule
import me.offeex.bloomware.client.setting.settings.*

object ModuleConfig : Config() {
    override val filePath = FilePath.MODULES

    override fun save() = JsonArray().apply {
        ModuleManager.modules.forEach { module ->

            val obj = JsonObject()

            // Adding module's name, enabled state and key to the config
            obj.addProperty("module", module.name)
            obj.addProperty("enabled", module.enabled)
            obj.addProperty("bind", module.key)

            // Adding hud module's position on the screen to the config
            if (module is HudModule) {
                obj.addProperty("posX", module.x)
                obj.addProperty("posY", module.y)
            }

            // Adding all settings to the config
            module.flattenSettings().forEach {
                when (it) {
                    is SettingBool -> apply {
                        if (it is SettingGroup && !it.toggleable) return@apply
                        obj.add(it.id, JsonPrimitive(it.toggled))
                    }

                    is SettingNumber -> obj.add(it.id, JsonPrimitive(it.value))
                    is SettingEnum -> obj.add(it.id, JsonPrimitive(it.selected))
                    is SettingColor -> obj.add(it.id, JsonPrimitive(it.color.argb))
                }
            }

            this.add(obj)
        }
    }

    override fun load(element: JsonElement) {
        val array = element.asJsonArray ?: return

        for (entry in array) try {

            // Retrieving module by name
            val obj = entry.asJsonObject
            val moduleName = tryParse("Error parsing `module.name` to String") {
                obj.get("module").asString
            }
            val module = ModuleManager.getModuleByName(moduleName) ?: continue

            val enabled = tryParse("Error parsing `module.enabled` to Boolean") {
                obj.get("enabled").asBoolean
            }
            if (enabled) module.enable()

            module.key = tryParse("Error parsing `module.bind` to Int") {
                obj.get("bind").asInt
            }

            if (module is HudModule) tryParse("Error parsing `hudModule.coord` to Int") {
                module.x = obj.get("posX").asInt
                module.y = obj.get("posY").asInt
            }

            for (m in module.flattenSettings()) obj.get(m.id)?.apply {
                try {
                    when (m) {
                        is SettingBool -> run {
                            if (m is SettingGroup && !m.toggleable) return@run
                            m.toggled = asBoolean
                        }

                        is SettingEnum -> m.selected = asString
                        is SettingNumber -> m.value = asDouble
                        is SettingColor -> m.color.setColor(asInt)
                    }
                } catch (e: IllegalArgumentException) {
                    Bloomware.LOGGER.error("Illegal value {$this} for setting ${m.id} in ${module.name}")
                }
            } ?: continue
        } catch (e: Exception) {
            Bloomware.LOGGER.error("Error while loading module: $e")
            e.printStackTrace()
        }
    }
}