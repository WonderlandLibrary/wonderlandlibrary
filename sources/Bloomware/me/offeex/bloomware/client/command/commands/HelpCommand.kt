package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.manager.managers.CommandManager.commands
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.arguments.CommandArgument
import net.minecraft.util.Formatting
import net.minecraft.util.Formatting.*

object HelpCommand : Command("help", {
	runs { commands.forEach { addMessage(GREEN + it.name + RESET + " - " + it.description) } }
	val cmd by CommandArgument.get(this)
	runs { addMessage(cmd.description) }
}) {
	override val description = "shows all available commands with their descriptions"
}