package me.offeex.bloomware.client.setting

import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.client.setting.settings.*

fun SettingsContainer.setting(
	name: String,
	description: String = "",
) = Setting.Builder(this, name, description)

abstract class Setting(val name: String, var description: String) : Cloneable {
	lateinit var id: String
	lateinit var parent: SettingsContainer

	private val updateValueBuses: MutableList<SettingValueUpdateBus<*>> = mutableListOf()
	val updateBus = SettingUpdateBus() // Broadcast setting state to GUI

	var active = true
		private set
	private var dependencies: List<Setting> = listOf()
	private lateinit var activityResolver: () -> Boolean

	open fun depend(vararg dependencies: Setting, resolver: () -> Boolean): Setting {
		for (d in dependencies) for (bus in d.updateValueBuses) bus.subscribe { _, _ -> updateActive() } // Update state on dependency change

		this.dependencies = dependencies.toList()
		activityResolver = resolver
		updateActive() // Set initial state

		return this
	}

	private fun updateActive() {
		active = activityResolver()
		updateBus.trigger()
	}

	protected fun <T> registerValueUpdateBus(bus: SettingValueUpdateBus<T>): SettingValueUpdateBus<T> {
		updateValueBuses.add(bus)
		return bus
	}

	abstract fun clone(name: String = this.name, description: String = this.description, parent: SettingsContainer = this.parent): Setting

    class Builder(
	    private val sc: SettingsContainer,
	    private val n: String,
	    private val d: String,
	) {
		private fun <S : Setting> setup(s: S): S = s.apply { sc.addSetting(this) }

        fun bool(toggled: Boolean = false) = setup(SettingBool(n, d, toggled))

        fun enum(vararg modes: String): SettingEnum {
            return setup(SettingEnum(n, d, modes[0], modes.toList()))
        }

        fun number(
            value: Number = 0.0,
            min: Number = Double.MIN_VALUE,
            max: Number = Double.MAX_VALUE,
            inc: Number = 1.0,
        ) = setup(SettingNumber(n, d, value.toDouble(), min.toDouble(), max.toDouble(), inc.toDouble()))

        fun color(hex: Long /* это пиздец :) */) = color(ColorMutable(hex))
        fun color(r: Int, g: Int, b: Int, a: Int = 255) = color(ColorMutable(r, g, b, a))
        fun color(color: ColorMutable = ColorMutable.WHITE) = setup(SettingColor(n, d, color))

        fun group(
			toggled: Boolean? = null,
        ): SettingGroup = setup(SettingGroup(n, d, toggled ?: false, toggled != null))

        fun map(key: Any, value: Boolean = false) = setup(SettingMap(n, d, key, value))
    }
}
