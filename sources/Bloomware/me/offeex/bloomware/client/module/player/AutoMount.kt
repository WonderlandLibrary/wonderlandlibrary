package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.DonkeyEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.entity.passive.LlamaEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.util.Hand

object AutoMount : Module("AutoMount", "Automatically mounts entities", Category.PLAYER) {
	private val allowed = setting("Allowed").group()
	private val boats = allowed.setting("Boats").bool()
	private val donkeys = allowed.setting("Donkeys").bool(true)
	private val llamas = allowed.setting("Llamas").bool(true)
	private val pigs = allowed.setting("Pigs").bool()
	private val horses = allowed.setting("Horses").bool(true)
	private val range = setting("Range").number(4.0, 1.0, 5.0)

	override fun onTick() {
		if (cPlayer.isRiding || cPlayer.vehicle != null) return
		var nearest = cWorld.entities.iterator().next()
		var interact = false
		cWorld.entities.forEach {
			if (isValid(it)) {
				nearest = it
				interact = true
			}
		}
		if (interact) cInteractManager.interactEntity(cPlayer, nearest, Hand.MAIN_HAND)
	}

	private fun isValid(entity: Entity): Boolean {
		return if (cPlayer.distanceTo(entity) >= range.value) false
		else entity is PigEntity && pigs.toggled || entity is BoatEntity && boats.toggled || entity is HorseEntity && horses.toggled || entity is DonkeyEntity && donkeys.toggled || entity is LlamaEntity && llamas.toggled
	}
}