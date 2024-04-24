package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.AccountManager.withIp
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.command.Command
import net.minecraft.text.Text

object LoginCommand : Command("login", {
	runs {
		val address = mc.currentServerEntry!!.address
		if (address != null) {
			val acc = cPlayer.entityName withIp address
			acc?.apply {
				addMessage("Found credentials to account! Logging in...")
				cPlayer.sendMessage(Text.of("/login " + acc.password), false)
			} ?: addMessage("Credentials not found!")
		} else addMessage("You are in singleplayer!")
	}
}) {
	override val description = "automatically logins to the server using found credentials"
}