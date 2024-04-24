package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender

object CustomFOV : Module("CustomFOV", "Changes your FOV.", Category.TWEAKS) {
	private val fov = setting("FOV").number(110.0, 30.0, 170.0, 1.0)

	private var oldFov = 90

	override fun onEnable() {
		oldFov = mc.options.fov.value
	}

	override fun onDisable() {
		mc.options.fov.value = oldFov
	}

	@Subscribe
	private fun onWorldRender(event: EventRender.World) {
		mc.options.fov.value = fov.value.toInt()
	}
}