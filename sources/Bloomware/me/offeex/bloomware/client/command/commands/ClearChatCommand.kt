package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.command.Command

object ClearChatCommand : Command("clearchat", {
	runs { mc.inGameHud.chatHud.clear(true) }
}) {
	override val description = "clears your chat"
}