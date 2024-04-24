package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.sound.SoundEvents

object SoundTracker : Module("SoundTracker", "popbob sex dupe.", Category.WORLD) {
	private val thunder = setting("ThunderSound").bool(true)
	private val endPortal = setting("EndPortalSound").bool(true)

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is PlaySoundS2CPacket) {
			val packet = event.packet
			if (packet.sound === SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER && thunder.toggled) addMessage("Thunder sound detected -> " + packet.x + ", " + packet.y + ", " + packet.z)
			if (packet.sound === SoundEvents.BLOCK_END_PORTAL_SPAWN && endPortal.toggled) addMessage("End portal sound detected -> " + packet.x + ", " + packet.y + ", " + packet.z)
		}
	}
}