package me.offeex.bloomware.client.setting

import me.offeex.bloomware.client.setting.settings.SettingGroup

interface SettingsContainer {
	// So fucking awful
	val nameContainer: String
	val settings: MutableList<Setting>

	fun <T : Setting> addSetting(setting: T) {
		setting.parent = this

		fun resolvePath(s: Setting): String {
			var path = s.name
			var parent = s.parent
			while (parent is SettingGroup) {
				path = parent.name + "." + path
				parent = parent.parent
			}
			return parent.nameContainer + "." + path
		}

		setting.id = resolvePath(setting)
		settings.add(setting)
	}

	fun flattenSettings(sets: MutableList<Setting> = mutableListOf()): MutableList<Setting> {
		sets.addAll(settings.filter { it !is SettingGroup || it.toggleable })
		settings.filterIsInstance<SettingGroup>().forEach { it.flattenSettings(sets) }
		return sets
	}
}