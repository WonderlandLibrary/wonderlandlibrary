package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket

object XCarry : Module("XCarry", "Allows you to keep items in crafting slots", Category.PLAYER) {
	private var lastPacket: CloseHandledScreenC2SPacket? = null

	override fun onDisable() {
		if (lastPacket != null) sendPacket(lastPacket!!)
		lastPacket = null
	}

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (event.packet is CloseHandledScreenC2SPacket) {
			val packet = event.packet
			if (packet.syncId != cPlayer.playerScreenHandler.syncId) return
			event.canceled = true
			lastPacket = packet
		}
	}
}