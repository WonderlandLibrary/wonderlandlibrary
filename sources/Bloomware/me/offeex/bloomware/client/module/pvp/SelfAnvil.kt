package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.api.manager.managers.RotationManager.reset
import me.offeex.bloomware.api.manager.managers.RotationManager.sendPacket
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction

object SelfAnvil : Module("SelfAnvil", "Burrows you with anvil", Category.PVP) {
    private val rotate = setting("Rotate").bool()
    private val autoSwitch = setting("AutoSwitch").bool(true)
    private val autoCenter = setting("AutoCenter").bool(true)

    override fun onEnable() {
        val slot = Items.ANVIL.findHotbarSlot()
        if (slot == -1) {
            ChatUtil.failMessage("There are no anvils in your hotbar, disabling!", this)
        } else if (!cPlayer.blockStateAtPos.isAir) return
        else {
            val blockPos = cPlayer.blockPos.up(2)
            val pos = cPlayer.pos.offset(Direction.UP, 2.0)
            if (autoCenter.toggled) MovementUtil.center()

            val oldSlot = cPlayer.inventory.selectedSlot
            cPlayer.inventory.selectedSlot = slot

            if (rotate.toggled) {
                RotationManager.setRotation(pos)
                if (RotationUtil.shouldRotate()) sendPacket()
            }
            cInteractManager.interactBlock(
                cPlayer, Hand.MAIN_HAND, BlockHitResult(pos, Direction.UP, blockPos, false)
            )
            cPlayer.swingHand(Hand.MAIN_HAND)
            if (rotate.toggled) reset()
            if (autoSwitch.toggled) cPlayer.inventory.selectedSlot = oldSlot
        }
        disable()
    }
}