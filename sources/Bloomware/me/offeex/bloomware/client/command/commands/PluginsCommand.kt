package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket
import org.apache.commons.lang3.StringUtils

object PluginsCommand : Command("plugins", {
	runs {
		addMessage("Grabbing server plugins...")
		Bloomware.EVENTBUS.register(this)
		cNetHandler.sendPacket(RequestCommandCompletionsC2SPacket(0, "/"))
	}
}) {
	override val description = "grabs server plugins"

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is CommandSuggestionsS2CPacket) {
			val commands = mutableListOf<String>()
			for (suggestion in event.packet.suggestions.list) {
				val plugin = suggestion.text.split(":")[0]
				if (suggestion.text.contains(":") && !plugin.equals(
						"minecraft",
						ignoreCase = true
					) && !plugin.equals("bukkit", ignoreCase = true) && !commands.contains(plugin)
				) commands.add(plugin)
			}
			addMessage("Plugins (" + commands.size + "): " + StringUtils.join(commands, ", "))
			Bloomware.EVENTBUS.unregister(this)
		}
	}
}