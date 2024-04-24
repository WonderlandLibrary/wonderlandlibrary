package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

class SettingGroup(n: String, d: String, t: Boolean, val toggleable: Boolean) : SettingBool(n, d, t), SettingsContainer {
    // So fucking awful
    override val nameContainer: String
        get() = super.name

    override val settings: MutableList<Setting> = mutableListOf()

    override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) = also { super.depend(*dependencies, resolver = resolver) }

    override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).group(toggled).also {
//        it.settings.addAll(settings)
        // TODO: Children-cloning
    }

    operator fun get(name: String) = settings.firstOrNull { it.name == name }
}
