package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.RotationManager.reset
import me.offeex.bloomware.api.manager.managers.RotationManager.sendPacket
import me.offeex.bloomware.api.manager.managers.RotationManager.setRotation
import me.offeex.bloomware.api.util.BlockUtil
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.item.Items
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object AutoAnvil : Module("AutoAnvil", "Places anvils above your enemies", Category.PVP) {
	private val range = setting("Range").number(3.0, 1.0, 5.0, 0.1)
	private val delay = setting("TickDelay").number(3.0, 1.0, 20.0, 1.0)
	private val sort = setting("Sort").enum("Health", "Distance")
	private val placement = setting("Placement").enum("AirPlace", "Strict")
	private val upDistance = setting("UpDistance").number(2.0, 1.0, 4.0, 1.0)
	private val rotate = setting("Rotate").bool()
	private val fov = setting("FOV").number(90.0, 0.0, 180.0, 1.0)
	private val targets = setting("Targets").group()
	private val players = targets.setting("Players").bool(true)
	private val friends = targets.setting("Friends").bool()
	private val animals = targets.setting("Animals").bool()
	private val hostiles = targets.setting("Hostiles").bool(true)
	private val disableOnDeath = setting("DisableOnDeath").bool(true)
	override fun onDisable() {
		reset()
	}

	override fun onTick() {
		val target = WorldUtil.target(
			range.value, sort.selected, players.toggled, friends.toggled, hostiles.toggled, animals.toggled
		)
		if (target == null || RotationUtil.yawToPos(target.pos) > fov.value) {
			reset()
			return
		}
		if (rotate.toggled) {
			setRotation(target.eyePos)
			if (RotationUtil.shouldRotate()) sendPacket()
		}
		cPlayer.apply {
			if (age % delay.value == 0.0) {
				val pos = target.blockPos.up(upDistance.value.toInt() + 1)
				val slot = Items.ANVIL.findHotbarSlot()
				val hand = Items.ANVIL.findHand()
				if (cWorld.getBlockState(pos).isAir && hand != null) {
					val dir = BlockUtil.findBlockSide(pos)
					if (dir == null && !placement.like("AirPlace")) return
					if (slot.toInt() != -1) inventory.selectedSlot = slot.toInt()
					cInteractManager.interactBlock(
						this, hand, BlockHitResult(
							Vec3d.of(pos),
							if (dir != null && placement.like("Strict")) dir else Direction.DOWN,
							pos,
							false
						)
					)
					swingHand(hand)
				}
			}
			if (isDead && disableOnDeath.toggled) disable()
		}
	}
}