package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen

object Hud : Module("HUD", "Edit your HUD", Category.CLIENT) {
	override fun onEnable() {
		if (!(mc.currentScreen is TitleScreen || mc.currentScreen is MultiplayerScreen)) mc.setScreen(Bloomware.hud)
		else Bloomware.currentScreen = Bloomware.hud
		disable()
	}
}