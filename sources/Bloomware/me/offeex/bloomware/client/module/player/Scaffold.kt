package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager.reset
import me.offeex.bloomware.api.manager.managers.RotationManager.setRotation
import me.offeex.bloomware.api.util.BlockUtil
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventClipAtLedge
import me.offeex.bloomware.event.events.EventInteract
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.item.BlockItem
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object Scaffold : Module("Scaffold", "Allows you to place blocks under you", Category.PLAYER) {
	private val placeDelay = setting("PlaceDelay").number(2.0, 1.0, 20.0, 1.0)
	private val airPlace = setting("AirPlace").bool()
	private val autoSwitch = setting("AutoSwitch").bool(true)
	private val rotate = setting("Rotate").bool()
	private val safeWalk = setting("SafeWalk").bool()
	private val tower = setting("Tower").bool()
	private val render = setting("Render").group(false)
	private val color = render.setting("Color").color(0, 0, 255, 100)
	private val renderMode = render.setting("Mode").enum("Fill", "Outline")
	private val lineWidth = render.setting("Width").number(4.0, 1.0, 5.0, 0.1)
	private lateinit var downPos: BlockPos
	private var shouldRender = false

	override fun onDisable() = reset()

	override fun onTick() {
		shouldRender = false
		cPlayer.apply {
			downPos = blockPos.down()
			if (BlockUtil.isPlaceable(downPos, true) && age % placeDelay.value == 0.0) {
				val oldSlot = inventory.selectedSlot
				val hand = findHand<BlockItem>()
				val slot =
					if (hand == Hand.MAIN_HAND) inventory.selectedSlot else findHotbarSlot<BlockItem>()
				if (slot != -1 && hand != Hand.OFF_HAND && autoSwitch.toggled) inventory.selectedSlot = slot
				if (hand != null) {
					if (rotate.toggled) setRotation(Vec3d.of(downPos).add(0.5, 1.0, 0.5))
					if (tower.toggled && mc.options.jumpKey.isPressed) setVelocity(0.0, 0.42, 0.0)
					val dir = BlockUtil.findBlockSide(downPos)
					if (dir == null && !airPlace.toggled) return
					val result = BlockHitResult(
						Vec3d.of(downPos),
						if (dir != null) dir.opposite else Direction.DOWN,
						if (dir != null) downPos.offset(dir) else downPos,
						false
					)
					cInteractManager.interactBlock(cPlayer, hand, result)
					swingHand(hand)
					if (autoSwitch.toggled) inventory.selectedSlot = oldSlot
				} else reset()
			}
		}
	}

	@Subscribe
	private fun onInteractBlock(event: EventInteract.Block) {
		shouldRender = true
	}

	@Subscribe
	private fun onWorldRender(event: EventRender.World) {
		if (!shouldRender || !render.toggled) return
		event.matrices.use {
			RenderUtil.translateToCamera(event.matrices, downPos)
			if (renderMode.like("Fill")) RenderUtil.drawFilledBox(event.matrices, Box(downPos), color.color)
			else RenderUtil.drawOutline(event.matrices, Box(downPos), color.color, lineWidth.value)
		}
	}

	@Subscribe
	private fun onClipAtLedge(event: EventClipAtLedge) {
		if (safeWalk.toggled) event.clip = true
	}
}