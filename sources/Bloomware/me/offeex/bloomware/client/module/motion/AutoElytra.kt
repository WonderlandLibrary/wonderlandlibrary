package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.InventoryUtil
import me.offeex.bloomware.api.util.InventoryUtil.findSlots
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ElytraItem
import net.minecraft.item.Items.ELYTRA
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket

object AutoElytra : Module("AutoElytra", "Automatically equips elytra if you are falling", Category.MOTION) {
    private val equip = setting("Equip").bool(true)
    private val takeOff = setting("TakeOff").bool(true)
    private val fallDistance = setting("FallDistance").number(0.0, 0.0, 100.0, 1.0)
    private val autoDisable = setting("AutoDisable").bool(true)

    override fun onTick() {
        cPlayer.apply {
            if (fallDistance >= this@AutoElytra.fallDistance.value) {
                if (!getEquippedStack(EquipmentSlot.CHEST).isOf(ELYTRA) && equip.toggled) {
                    val slot = findSlots<ElytraItem>().firstOrNull()
                    if (slot != null) InventoryUtil.swap(slot, 6)
                    if (autoDisable.toggled) disable()
                }
                if (!isOnGround && !isFallFlying && !isTouchingWater && takeOff.toggled) {
                    sendPacket(
                        ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING)
                    )
                    if (autoDisable.toggled) disable()
                }
            }
        }
    }
}