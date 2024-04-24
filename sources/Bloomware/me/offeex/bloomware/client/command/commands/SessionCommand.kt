package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.api.manager.managers.SessionManager.convertTime
import me.offeex.bloomware.api.manager.managers.SessionManager.kills
import me.offeex.bloomware.api.manager.managers.SessionManager.timeOnline
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.command.Command
import net.minecraft.util.Formatting.*

object SessionCommand : Command("session", {
	runs {
		addMessage(
			String.format(
				"Your current session stats: \n$GREEN%s kills, \n$RED%s deaths, \n$GOLD%s pops, \n${RESET}Online for $AQUA%s",
				kills,
				SessionManager.deaths,
				SessionManager.pops,
				convertTime(timeOnline)
			)
		)
	}
}) {
	override val description = "shows info about your current session"
}