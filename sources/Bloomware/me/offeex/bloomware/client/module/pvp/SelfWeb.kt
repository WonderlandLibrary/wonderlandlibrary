package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.api.manager.managers.RotationManager.sendPacket
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.item.Items
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object SelfWeb : Module("SelfWeb", "Automatically places web on your feet", Category.PVP) {
	private val autoSwitch = setting("AutoSwitch").bool(true)
	private val autoDisable = setting("AutoDisable").bool(true)
	private val autoCenter = setting("AutoCenter").bool(true)
	private val rotate = setting("Rotate").bool()

	override fun onEnable() {
		val hand = Items.COBWEB.findHand()
		if (hand == null) {
			addMessage(Formatting.RED + "Cobweb was not found in your hotbar, disabling!")
			disable()
			return
		}
		if (autoCenter.toggled) MovementUtil.center()
	}

	override fun onTick() {
		cPlayer.apply {
			val slot = Items.COBWEB.findHotbarSlot()
			val hand = Items.COBWEB.findHand()
			val oldSlot = inventory.selectedSlot
			if (hand == null || !blockStateAtPos.isAir || !isOnGround) return
			if (rotate.toggled) {
				RotationManager.setRotation(pos.offset(Direction.DOWN, 1.0))
				if (RotationUtil.shouldRotate()) sendPacket()
			}
			inventory.selectedSlot = slot
			cInteractManager.interactBlock(
				cPlayer, hand, BlockHitResult(
					Vec3d.of(
						blockPos
					), Direction.DOWN, blockPos, false
				)
			)
			swingHand(hand)
			if (autoSwitch.toggled) inventory.selectedSlot = oldSlot
			if (autoDisable.toggled) disable()
		}
	}
}