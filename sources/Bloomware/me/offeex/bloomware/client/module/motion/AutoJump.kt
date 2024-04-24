package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object AutoJump : Module("AutoJump", "Automatically jumps", Category.MOTION) {
	private var legit = setting("Legit").bool()
	private var onGround = setting("OnGround").bool(true)

	override fun onDisable() {
		mc.options.jumpKey.isPressed = false
	}

	override fun onTick() {
		if (!cPlayer.isOnGround && onGround.toggled) return
		if (legit.toggled && mc.currentScreen == null) mc.options.jumpKey.isPressed = true else cPlayer.jump()
	}
}