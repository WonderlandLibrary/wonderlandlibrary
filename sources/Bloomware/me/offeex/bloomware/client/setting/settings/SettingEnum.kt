package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingValueUpdateBus
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

class SettingEnum(n: String, d: String, s: String, val modes: List<String>) : Setting(n, d) {
	var selected = s
		set(value) {
			require(modes.contains(value))
			val oldValue = field
			field = value
			selectedUpdateBus.trigger(oldValue, field)
		}
	val selectedUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<String>())

	infix fun like(value: String) = selected == value

	override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) =
		also { super.depend(*dependencies, resolver = resolver) }

	override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).enum(*modes.toTypedArray()).also {
		it.selected = selected
	}
}