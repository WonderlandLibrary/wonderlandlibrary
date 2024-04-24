package me.offeex.bloomware.client.command.commands

import dev.nicolai.brigadier.arguments.greedyString
import dev.nicolai.brigadier.arguments.string
import me.offeex.bloomware.api.manager.managers.CommandManager
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.command.Command

object PrefixCommand : Command("prefix", {
	runs { addMessage("Current prefix is ${CommandManager.prefix}") }
	val prefix by greedyString("prefix")
	runs {
		CommandManager.prefix = prefix
		addMessage("Prefix set to $prefix")
	}
}) {
	override val description = "sets prefix for commands"
}