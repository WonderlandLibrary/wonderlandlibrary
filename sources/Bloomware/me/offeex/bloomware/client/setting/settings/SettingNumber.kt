package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingValueUpdateBus
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

class SettingNumber(n: String, d: String, v: Double, min: Double, max: Double, inc: Double) : Setting(n, d) {
	val valueUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Double>())
	val minUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Double>())
	val maxUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Double>())
	val incUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<Double>())

	var value = v
		set(value) {
			require(value in min..max)
			val oldValue = field
			field = value
			valueUpdateBus.trigger(oldValue, field)
		}
	var min = min
		set(value) {
			val oldValue = field
			field = value
			minUpdateBus.trigger(oldValue, field)
		}
	var max = max
		set(value) {
			val oldValue = field
			field = value
			maxUpdateBus.trigger(oldValue, field)
		}
	var inc = inc
		set(value) {
			val oldValue = field
			field = value
			incUpdateBus.trigger(oldValue, field)
		}
	val defaultValue: Double = v

	override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) = also { super.depend(*dependencies, resolver = resolver) }

	override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).number(value, min, max, inc)
}