package me.offeex.bloomware.client.command.commands

import dev.nicolai.brigadier.arguments.string
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.errorMessage
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.arguments.ModuleArgument
import me.offeex.bloomware.client.command.arguments.SettingArgument
import me.offeex.bloomware.client.command.arguments.SettingValueArgument
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.settings.SettingBool
import me.offeex.bloomware.client.setting.settings.SettingEnum
import me.offeex.bloomware.client.setting.settings.SettingGroup
import me.offeex.bloomware.client.setting.settings.SettingNumber
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper

object SetCommand : Command("set", {
	val m by ModuleArgument.get(this)
	val setting by SettingArgument.get(this)
	val value by SettingValueArgument.get(this)

	fun addMsg(m: Module, s: Setting, newVal: Any) {
		addMessage(Formatting.GREEN + s.name + " in " + m.name + " has been set to " + newVal)
	}

	runs {
		val s = setting
		when {
			s is SettingBool -> {
				if (s is SettingGroup && !s.toggleable) return@runs
				s.toggled = value.toBoolean()
				addMsg(m, s, s.toggled)
			}

			s is SettingEnum && s.modes.any { it.equals(value, true) } -> {
				s.selected = value
				addMsg(m, s, s.selected)
			}

			s is SettingNumber && value.toDoubleOrNull() != null -> {
				s.value = MathHelper.clamp(value.toDouble(), s.min, s.max)
				addMsg(m, s, s.value)
			}
		}
	}
}) {
	override val description: String = "allows you to configure module settings"
}