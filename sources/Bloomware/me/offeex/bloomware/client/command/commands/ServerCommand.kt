package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.commands.ServerCommand.server
import net.minecraft.util.Formatting.*

object ServerCommand : Command("server", {
	runs { addMessage("You are playing on $GREEN$server") }
	literal("copy").runs {
		mc.keyboard.clipboard = server
		addMessage("Server IP was copied to your clipboard!")
	}
}) {
	override val description = "shows server info you playing"

	private val server: String
		get() = if (mc.currentServerEntry != null) mc.currentServerEntry!!.address
		else if (mc.isInSingleplayer) "Singleplayer"
		else "null"
}