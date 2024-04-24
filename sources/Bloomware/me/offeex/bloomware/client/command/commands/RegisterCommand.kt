package me.offeex.bloomware.client.command.commands

import dev.nicolai.brigadier.arguments.greedyString
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.AccountManager.accounts
import me.offeex.bloomware.api.manager.managers.AccountManager.with
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.generateRandomPassword
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.command.Command

object RegisterCommand : Command("register", {
	literal("generate").runs {
		addMessage("Generating random password...")
		val password = generateRandomPassword()
		accounts.add(cPlayer.entityName with password)
		addMessage("Generated a new password: $password! Copied to clipboard.")
		mc.keyboard.clipboard = password
	}
	literal("add") {
		val password by greedyString("password")
		runs {
			accounts.add(cPlayer.entityName with password)
			addMessage("Your account was saved! Password was copied to clipboard!")
			mc.keyboard.clipboard = password
		}
	}
}) {
	override val description = "registers you to the server and saves credentials"
}