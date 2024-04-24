package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.api.helper.Saveable
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import java.io.PrintWriter
import kotlin.random.Random

object Spammer : Module("Spammer", "Spams the chat.", Category.NETWORK), Saveable {
	private val mode = setting("Mode").enum("Order", "Random")
	private val messageType = setting("MessageType").enum("File", "PrioBan")
	private val delay = setting("Delay").number(1.0, 0.0, 60.0, 0.5)
	val messages = mutableListOf<String>()
	private val prioBanMsgs = listOf(
		"I love WALDEN",
		"u mad?",
		"love walden",
		"fuck you hause",
		"hause is a fucking cokesniffer",
		"don't buy prio, hause is going to spend this money on coke",
		"money from prio = new doses of coke",
		"i love building lag machines",
		"unban terpila"
	)

	private var count = 0

	init {
		if (FilePath.SPAMMER.file.exists()) messages.addAll(FilePath.SPAMMER.file.readLines())
	}

	override fun onTick() {
		val msgs = when (messageType.selected) {
			"File" -> messages
			"PrioBan" -> prioBanMsgs
			else -> return
		}
		if (cPlayer.age % (delay.value * 20) == 0.0) {
			when (mode.selected) {
				"Order" -> {
					cNetHandler.sendChatMessage(msgs[count])
					count++
				}
				"Random" -> cNetHandler.sendChatMessage(msgs[Random.nextInt(msgs.size)])
			}
		}
		if (count == msgs.size - 1) count = 0
	}

	override fun save() {
		PrintWriter(FilePath.SPAMMER.file.printWriter(), true).use { writer ->
			messages.forEach { writer.write(it + "\n") }
		}
	}

}