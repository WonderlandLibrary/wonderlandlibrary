package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object AutoWalk : Module("AutoWalk", "Allows you to go automatically", Category.MOTION) {
	val mode = setting("Mode").enum("Normal")

	override fun onTick() {
		if (mode.like("Normal")) mc.options.forwardKey.isPressed = true
	}

	override fun onDisable() {
		if (mode.like("Normal")) mc.options.forwardKey.isPressed = false
	}
}