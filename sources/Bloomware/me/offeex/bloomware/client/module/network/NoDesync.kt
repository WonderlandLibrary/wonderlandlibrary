package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket

object NoDesync : Module("NoDesync", "Prevents you from desyncing", Category.NETWORK) {
	private val ids = mutableListOf<Int>()

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (mc.isInSingleplayer) return
		if (event.packet is TeleportConfirmC2SPacket) {
			val packet = event.packet
			ids.add(packet.teleportId)
		}
	}

	public override fun onEnable() {
		ids.clear()
	}

	override fun onTick() {
		if (mc.isInSingleplayer) return
		if (ids.isNotEmpty()) {
			sendPacket(TeleportConfirmC2SPacket(ids[0]))
			ids.removeAt(0)
		}
	}
}