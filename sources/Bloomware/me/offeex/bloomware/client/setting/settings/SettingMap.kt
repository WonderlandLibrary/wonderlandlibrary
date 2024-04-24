package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingValueUpdateBus
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

class SettingMap(n: String, d: String = "", k: Any, v: Boolean) : SettingBool(n, d, v) {
	var key = k
		set(value) {
			val oldValue = field
			field = value
			keyUpdateBus.trigger(oldValue, field)
		}
	val keyUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Any>())

	override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) = also { super.depend(*dependencies, resolver = resolver) }

	override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).map(key, toggled)
}