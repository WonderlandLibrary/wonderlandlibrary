package net.minusmc.ravenb4.module

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minusmc.ravenb4.setting.Setting
import net.minusmc.ravenb4.setting.impl.TickSetting
import org.lwjgl.input.Keyboard

open class Module(val name: String, val category: ModuleCategory, val description: String = "") {
    protected val settings = mutableListOf<Setting<*>>()
    var enabled = false
    protected val defaultEnabled = enabled

    private var keycode = 0
    protected val defualtKeyCode = keycode

    protected val mc = Minecraft.getMinecraft()
    private var isToggled = false

    fun addSetting(setting: Setting<*>) {
        settings.add(setting);
    }

    fun getConfigAsJson(): JsonObject {
        val settings = JsonObject()
        for (setting in this.settings) {
            val settingData: JsonElement = setting.getConfigAsJson()
            settings.add(setting.name, settingData)
        }
        val data = JsonObject()
        data.addProperty("enabled", enabled)
        data.addProperty("keycode", keycode)
        data.add("settings", settings)
        return data
    }

    fun applyConfigFromJson(data: JsonObject) {
        try {
            keycode = data["keycode"].asInt
            setToggled(data["enabled"].asBoolean)
            val settingsData = data["settings"].getAsJsonObject()
            for (setting in this.settings) {
                if (settingsData.has(setting.name)) {
                    setting.applyConfigFromJson(
                        settingsData[setting.name].getAsJsonObject()
                    )
                }
            }
        } catch (ignored: NullPointerException) {}
    }


    fun keybind() {
        if (keycode != 0 && canBeEnabled()) {
            if (!this.isToggled && Keyboard.isKeyDown(keycode)) {
                toggle()
                this.isToggled = true
            } else if (!Keyboard.isKeyDown(keycode)) {
                this.isToggled = false
            }
        }
    }

    fun canBeEnabled(): Boolean = true

    fun enable() {
        enabled = true
        onEnable()
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun disable() {
        enabled = false
        onDisable()
        MinecraftForge.EVENT_BUS.unregister(this)
    }

    fun setToggled(enabled: Boolean) {
        if (enabled) {
            enable()
        } else {
            disable()
        }
    }

    fun getSettingByName(name: String?): Setting<*>? {
        for (setting in settings) {
            if (setting.name.equals(name, true)) return setting
        }
        return null
    }

    fun registerSettings(vararg settings: Setting) = for (setting in settings) settings.add(setting)

    fun registerSetting(setting: Setting<*>) = settings.add(setting)

    open fun onEnable() {}

    open fun onDisable() {}

    fun toggle() {
        if (enabled) disable()
        else enable()
    }

    fun update() {}

    fun guiUpdate() {}

    fun guiButtonToggled(b: TickSetting?) {}

    fun getKeycode(): Int {
        return keycode
    }

    fun setbind(keybind: Int) {
        keycode = keybind
    }

    fun resetToDefaults() {
        keycode = defualtKeyCode
        setToggled(defaultEnabled)
        for (setting in settings) {
            setting.resetToDefaults()
        }
    }

    fun onGuiClose() {}

    fun getBindAsString(): String {
        return if (keycode == 0) "None" else Keyboard.getKeyName(keycode)
    }

    fun clearBinds() {
        keycode = 0
    }


}