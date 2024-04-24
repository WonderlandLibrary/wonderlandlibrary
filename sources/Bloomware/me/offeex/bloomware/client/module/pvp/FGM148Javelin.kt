package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.item.BowItem
import net.minecraft.item.CrossbowItem
import net.minecraft.item.Items.BOW
import net.minecraft.item.Items.CROSSBOW
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full
import net.minecraft.util.Hand

object FGM148Javelin : Module("FGM148Javelin", "Ukraine mode", Category.PVP) {
	private val bow = setting("Bow").bool(true)
	private val crossbow = setting("Crossbow").bool(true)
	private val stopwatch = Stopwatch()

	//    TODO: future...
	override fun onTick() {
		val cBowHand = findHand<CrossbowItem>()
		val bowHand = findHand<BowItem>()
		val activeItem = if (cBowHand != null) CROSSBOW else (if (bowHand != null) BOW else null)
		cPlayer.apply {
			networkHandler.apply {
				mc.options.apply {
					if (activeItem === CROSSBOW) {
						if (!CrossbowItem.isCharged(getStackInHand(cBowHand))) {
							useKey.isPressed = true
							if (stopwatch.passed((CrossbowItem.getPullTime(getStackInHand(cBowHand)) + 2) * 50L)) {
								useKey.isPressed = false
								stopwatch.reset()
							}
						} else {
							val t = WorldUtil.target(12.0, "Distance", true, false, true, true) ?: return
							sendPacket(Full(t.x, t.eyeY - 0.2, t.z, yaw, 90f, false))
							sendPacket(
								PlayerInteractItemC2SPacket(
									if (activeItem === CROSSBOW) cBowHand else if (activeItem === BOW) bowHand else Hand.MAIN_HAND,
									1
								)
							)
						}
					} else if (activeItem === BOW) {
						if (stopwatch.passed(200)) {
							val t = WorldUtil.target(12.0, "Distance", true, false, true, true) ?: return
							sendPacket(Full(t.x, t.eyeY - 0.2, t.z, yaw, 90f, false))
							sendPacket(PlayerInteractItemC2SPacket(cBowHand, 1))
							useKey.isPressed = false
							stopwatch.reset()
						} else useKey.isPressed = true
					}
				}
			}
		}
	}
}