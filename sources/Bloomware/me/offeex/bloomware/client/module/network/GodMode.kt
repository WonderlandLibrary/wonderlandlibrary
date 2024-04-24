package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.block.Blocks
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket

object GodMode : Module("GodMode", "Makes you invincible", Category.NETWORK) {
	private val mode = setting("Mode").enum("Portal")

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (mode.like("Portal") && inPortal()) {
			if (event.packet is TeleportConfirmC2SPacket) event.canceled = true
		}
	}

	private fun inPortal() = cPlayer.blockStateAtPos.isOf(Blocks.NETHER_PORTAL) || cPlayer.blockStateAtPos.isOf(Blocks.END_PORTAL)
}