package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventRender
import me.offeex.bloomware.event.events.EventScreen
import me.offeex.bloomware.event.events.EventUpdate
import net.minecraft.client.gui.screen.DeathScreen
import net.minecraft.client.gui.screen.DisconnectedScreen
import net.minecraft.entity.Entity
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket
import net.minecraft.world.GameMode

object DeathCam : Module("DeathCam", "explorer lol", Category.WORLD) {
	private val saveEntities = setting("SaveEntities").bool(true)
	private val coords = setting("Coords").group(true)
	private val dimension = coords.setting("Dimension").bool(true)

	private var health = 20f
	private var oldScreen: DeathScreen? = null
	private var dead = false
	private var ids: List<Entity> = emptyList()

	@Subscribe
	private fun onUpdateHealth(event: EventUpdate.Health) {
		health = event.health
	}

	override fun onEnable() {
		health = cPlayer.health
	}

	override fun onDisable() {
		if (health <= 0) mc.setScreenAndRender(oldScreen)
		if (ids.isNotEmpty()) ids.forEach { cWorld.removeEntity(it.id, Entity.RemovalReason.DISCARDED) }
		dead = false
	}

	@Subscribe
	private fun onDrawOverlay(event: EventRender.HUD) {
		if (health <= 0 && mc.currentScreen is DeathScreen) {
			cPlayer.health = 20f
			mc.setScreen(null)
			cInteractManager.setGameMode(GameMode.SPECTATOR)
			cPlayer.noClip = true
			dead = true
			ids = cWorld.entities.toList()
		} else ids = emptyList()
	}

	@Subscribe
	private fun onOpenScreen(event: EventScreen.Open) {
		if (event.screen is DeathScreen) {
			oldScreen = event.screen as DeathScreen
			if (coords.toggled) {
				var msg = "You died at ${String.format("%.1f", cPlayer.x)} ${String.format("%.1f", cPlayer.z)}"
				if (dimension.toggled) msg += " in " + convert(WorldUtil.dimension)
				ChatUtil.addMessage(msg)
			}
		} else if (event.screen is DisconnectedScreen) disable()
	}

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (event.packet is ClientStatusC2SPacket) {
			if (event.packet.mode == ClientStatusC2SPacket.Mode.PERFORM_RESPAWN) dead = false
		}
	}

	//    TODO: Clear saved entities
	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is EntitiesDestroyS2CPacket) event.canceled = dead && saveEntities.toggled
		if (event.packet is GameJoinS2CPacket) disable()
	}

	private fun convert(dimension: Int) = when (dimension) {
		0 -> "Overworld"
		1 -> "Nether"
		2 -> "End"
		else -> "Unknown"
	}
}
