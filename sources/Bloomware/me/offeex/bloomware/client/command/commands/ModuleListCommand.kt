package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.manager.managers.ModuleManager.modules
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.command.Command
import net.minecraft.util.Formatting

object ModuleListCommand : Command("modulelist", {
	runs {
		val moduleMsg = modules.joinToString(", ") { it.name }
		addMessage("Modules: " + Formatting.GRAY + moduleMsg)
	}
}) {
	override val description = "shows all available modules"
}