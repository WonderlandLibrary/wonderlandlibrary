package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.manager.managers.ModuleManager.modules
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.errorMessage
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.arguments.KeyArgument
import me.offeex.bloomware.client.command.arguments.KeyArgument.str
import me.offeex.bloomware.client.command.arguments.ModuleArgument
import me.offeex.bloomware.client.module.Module
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Formatting.*

object BindCommand : Command("bind", {
	fun Module.unbind(k: InputUtil.Key) {
		key = -1
		addMessage("$name was unbound from ${k.str()}!")
	}

	literal("reset") {
		val m by ModuleArgument.get(this)
		runs {
			if (m.key == -1) errorMessage("Module $AQUA${m.name} ${RESET}is not bound!")
			else m.unbind(InputUtil.fromKeyCode(m.key, -1))
		}
	}
	literal("reset") {
		val key by KeyArgument.get(this)
		runs {
			val ms = modules.filter { it.key == key.code }
			if (ms.isNotEmpty()) ms.forEach { it.unbind(key) }
			else errorMessage("No modules is bound to $BLUE${key.str().uppercase()}!")
		}
	}

	literal("set") {
		val m by ModuleArgument.get(this)
		val key by KeyArgument.get(this)
		runs {
			m.key = key.code
			addMessage("$AQUA${m.name} was bound to $BLUE${key.str().uppercase()}")
		}
	}
}) {
	override val description = "sets binds to modules"
}