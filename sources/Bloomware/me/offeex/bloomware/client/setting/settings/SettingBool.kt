package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingValueUpdateBus
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

open class SettingBool(n: String, d: String, t: Boolean) : Setting(n, d) {
	var toggled = t
		set(value) {
			val oldValue = field
			field = value
			toggledUpdateBus.trigger(oldValue, field)
		}
	val toggledUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Boolean>())

	override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) = also { super.depend(*dependencies, resolver = resolver) }

	override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).bool(toggled)
}