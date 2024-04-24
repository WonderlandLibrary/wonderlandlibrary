package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand

object AutoFish : Module("AutoFish", "Automatically fishing", Category.PLAYER) {
	val trigger = setting("Trigger").enum("Hook", "Sound")
	private val delay = setting("Delay").number(0.1, 0.0, 3.0, 0.01)
	private val stopwatch = Stopwatch()

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is PlaySoundS2CPacket) {
			val packet = event.packet
			if (packet.sound != SoundEvents.ENTITY_FISHING_BOBBER_SPLASH || !trigger.like("Sound")) cInteractManager.interactItem(
				cPlayer, Hand.MAIN_HAND
			)
		}
	}

	override fun onTick() {
		if (stopwatch.passed((delay.value * 1000).toLong()) && cPlayer.fishHook == null) {
			cInteractManager.interactItem(cPlayer, Hand.MAIN_HAND)
			stopwatch.reset()
		}
	}
}