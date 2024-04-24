package me.offeex.bloomware.client.command.commands

import dev.nicolai.brigadier.arguments.string
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.module.network.Spammer
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.useLines

object SpammerCommand : Command("spammer", {
	literal("add") {
		val msg by string("message")
		runs {
			Spammer.messages.add(msg)
		}
	}
	literal("remove") {
		val msg by string("message")
		runs {
			Spammer.messages.remove(msg)
		}
	}
	literal("set") {
		val path by string("path")
		runs {
			val p = Path(path)
			if (!p.exists() || !p.endsWith(".txt")) return@runs
			Spammer.messages.clear()
			p.useLines { Spammer.messages.addAll(it) }
		}
	}
}) {
	override val description = "allows you to modify spam messages"
}