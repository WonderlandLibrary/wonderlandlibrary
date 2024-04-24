package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventCamera

object ViewClip : Module("ViewClip", "Provides flexible settings for camera", Category.TWEAKS) {
	val distance = setting("Distance").number(4.0, 1.0, 15.0, 0.1)

	@Subscribe
	private fun onCamera(event: EventCamera.ClipToSpace) {
		event.cirValue = distance.value
	}
}