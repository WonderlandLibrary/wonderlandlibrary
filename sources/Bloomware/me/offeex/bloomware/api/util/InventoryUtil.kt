package me.offeex.bloomware.api.util

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

object InventoryUtil {

    /* *************** Search utils *************** */

    fun Item.findSlots(): List<Int> {
        return (0..cPlayer.inventory.size()).filter { cPlayer.inventory.getStack(it).isOf(this) }
    }

    inline fun <reified T : Item> findSlots(): List<Int> {
        return (0..cPlayer.inventory.size()).filter { cPlayer.inventory.getStack(it).item is T }
    }

    fun Item.findHotbarSlot(): Int {
        return (0..8).find { cPlayer.inventory.getStack(it).isOf(this) } ?: -1
    }

    inline fun <reified T : Item> findHotbarSlot(): Int {
        return (0..8).find { cPlayer.inventory.getStack(it).item is T } ?: -1
    }

    fun Item.findHand(): Hand? {
        return if (cPlayer.mainHandStack.isOf(this)) Hand.MAIN_HAND
        else if (cPlayer.offHandStack.isOf(this)) Hand.OFF_HAND
        else null
    }

    inline fun <reified T : Item> findHand(): Hand? {
        return if (cPlayer.mainHandStack.item is T) Hand.MAIN_HAND
        else if (cPlayer.offHandStack.item is T) Hand.OFF_HAND
        else null
    }

    fun findBestTool(state: BlockState): Int {
        var bestSlot = -1
        var damageMult = 0f

        val size = PlayerInventory.MAIN_SIZE
        for (i in (size..size + PlayerInventory.getHotbarSize())) {
            if (cPlayer.playerScreenHandler.getSlot(i).stack.item !is MiningToolItem) continue

            val damage = state.calcBlockBreakingDelta(cPlayer, cWorld, BlockPos.ORIGIN)

            if (damage > damageMult) {
                damageMult = damage
                bestSlot = i
            }
        }

        return if (damageMult <= 0 || bestSlot == -1) cPlayer.inventory.selectedSlot
        else bestSlot
    }

    fun Item.quickAccessible() = findHotbarSlot() != -1 || cPlayer.offHandStack.isOf(this)

    fun ItemStack.getDurability() = 1.0 - damage / maxDamage.toDouble()

    infix fun ItemStack.isOn(equip: EquipmentSlot) = item is ArmorItem && (item as ArmorItem).slotType === equip

    /* *************** Helper utils *************** */

    // made because of inventory.getStack() have different slot counting. And to match it, you have to add 36 to slot, so slot 1 will be 37
    fun getStack(slot: Int): ItemStack = cPlayer.currentScreenHandler.getSlot(slot).stack

    /* *************** Action utils *************** */

    fun swap(oldSlot: Int, newSlot: Int) {
        val sync = cPlayer.playerScreenHandler.syncId
        cInteractManager.clickSlot(sync, oldSlot, newSlot, SlotActionType.SWAP, cPlayer)
    }

    fun switchToItem(item: Item) = item.findHotbarSlot().also {
        if (it != -1) cPlayer.inventory.selectedSlot = it
    }

    inline fun <reified T : Item> switchToItem() = findHotbarSlot<T>().also {
        if (it != -1) cPlayer.inventory.selectedSlot = it
    }
}