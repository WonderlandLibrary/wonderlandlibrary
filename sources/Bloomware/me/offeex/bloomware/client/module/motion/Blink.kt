package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventWorld
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import java.util.*

object Blink : Module("Blink", "Allows you to server-side teleport", Category.MOTION) {
	private val renderPlayer = setting("RenderPlayer").bool(true)
	private val packets = mutableListOf<PlayerMoveC2SPacket>()
	private lateinit var player: OtherClientPlayerEntity

	override fun onEnable() {
		if (renderPlayer.toggled) {
			player = OtherClientPlayerEntity(mc.world, cPlayer.gameProfile)
			player.copyFrom(cPlayer)
			player.uuid = UUID.randomUUID()
			cWorld.addEntity(-300, player)
		}
	}

	override fun onDisable() {
		packets.forEach { sendPacket(it) }
		packets.clear()
		cWorld.removeEntity(player.id, Entity.RemovalReason.DISCARDED)
	}

	@Subscribe
	private fun onJoinWorld(event: EventWorld) {
		packets.clear()
		disable()
	}

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (event.packet is PlayerMoveC2SPacket) {
			val packet = event.packet
			event.canceled = true
			packets.add(packet)
		}
	}
}