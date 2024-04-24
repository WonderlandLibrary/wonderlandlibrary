package me.offeex.bloomware.client.setting.settings

import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingValueUpdateBus
import me.offeex.bloomware.client.setting.SettingsContainer
import me.offeex.bloomware.client.setting.setting

class SettingColor(n: String, d: String, c: ColorMutable) : Setting(n, d) {
	var color: ColorMutable = c
		set(value) {
			val oldValue = field
			field = value
			colorUpdateBus.trigger(oldValue, field)
		}

	fun color(c: ColorMutable) = also { color = c }
	fun color(r: Int, g: Int, b: Int, a: Int = 255) = also { color = ColorMutable(r, g, b, a) }
	fun color(setting: SettingColor) = also { color = setting.color }

	val colorUpdateBus = registerValueUpdateBus(SettingValueUpdateBus<ColorMutable>())

	init {
		color.onUpdate { colorUpdateBus.trigger(color, color) }
	}

	override fun depend(vararg dependencies: Setting, resolver: () -> Boolean) = also { super.depend(*dependencies, resolver = resolver) }

	override fun clone(name: String, description: String, parent: SettingsContainer) = parent.setting(name, description).color(color)
}