package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object UnfocusedCPU :
	Module("UnfocusedCPU", "Stops game rendering if you are not focused on window", Category.CLIENT) {
	private val limit = setting("FPSLimit").number(1.0, 1.0, 60.0, 1.0)

	override fun onDisable() {
		mc.window.framerateLimit = mc.options.maxFps.value
	}

	override fun onTick() {
		mc.window.framerateLimit = if (!mc.isWindowFocused) limit.value.toInt() else mc.options.maxFps.value
	}
}