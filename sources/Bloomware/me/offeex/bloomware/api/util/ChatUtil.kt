package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.client.module.Module
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Formatting.*
import kotlin.random.Random

object ChatUtil {
	private val PREFIX = "$BLACK$BOLD<$LIGHT_PURPLE${BOLD}Bloomware$BLACK$BOLD>$GRAY$BOLD ▸ "

	fun failMessage(message: String, module: Module) = addMessage(RED + message, module)
	fun errorMessage(message: String) = addMessage(DARK_RED + message)

	fun addMessage(message: String, module: Module) {
		addMessage("$AQUA${module.name} ▸ $RESET$message")
	}

	fun addMessage(message: String) {
		sendClientMsg(Text.literal(PREFIX).append(Text.literal(message)))
	}

	private fun sendClientMsg(message: Text) = Bloomware.mc.inGameHud.chatHud.addMessage(message)

	fun generateRandomPassword(): String {
		val password = StringBuilder()
		for (i in 0..9) password.append((Random.nextInt(26) + 'a'.code).toChar())
		return password.toString()
	}


	operator fun Formatting.plus(other: String) = this.toString() + other
}