package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventScreen
import net.minecraft.client.gui.screen.DeathScreen

object AutoRespawn : Module("AutoRespawn", "Allows you to respawn automatically", Category.WORLD) {
	@Subscribe
	private fun onOpenScreen(event: EventScreen.Open) {
		if (event.screen is DeathScreen) cPlayer.requestRespawn()
	}
}