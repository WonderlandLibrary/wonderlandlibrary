package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.PlayerUtil.getGamemode
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity

object AntiBots : Module("AntiBots", "Detects and removes bots from the world", Category.WORLD) {
	private val invisible = setting("Invisible").bool(true)
	private val gamemode = setting("NullGM").bool(true)
	private val entry = setting("NullEntry").bool(true)
	private val profile = setting("NullProfile").bool(true)

	override fun onTick() {
		cWorld.entities.filter { isBot(it) }
			.forEach { cWorld.removeEntity(it.id, Entity.RemovalReason.DISCARDED) }
	}

	private fun isBot(entity: Entity) =
		entity is PlayerEntity && (entity.getGamemode() == null && gamemode.toggled || cNetHandler.getPlayerListEntry(
			entity.getUuid()
		) == null && entry.toggled || cNetHandler.getPlayerListEntry(entity.getUuid())!!.profile == null && profile.toggled || entity.isInvisible()) && invisible.toggled
}