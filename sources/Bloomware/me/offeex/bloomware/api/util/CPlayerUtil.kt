package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.RotationManager.packetPitch
import me.offeex.bloomware.api.manager.managers.RotationManager.packetYaw
import me.offeex.bloomware.api.manager.managers.RotationManager.targetPitch
import me.offeex.bloomware.api.manager.managers.RotationManager.targetYaw
import me.offeex.bloomware.api.util.EntityUtil.hasEnchantment
import me.offeex.bloomware.api.util.EntityUtil.hasEnchantmentOnArmor
import me.offeex.bloomware.client.module.motion.Flight
import me.offeex.bloomware.client.setting.settings.SettingGroup
import me.offeex.bloomware.client.setting.settings.SettingMap
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.item.Items
import net.minecraft.item.ToolItem
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.Hand
import net.minecraft.util.UseAction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.sqrt

object CPlayerUtil {
    val isMining: Boolean
        get() {
            if (mc.crosshairTarget !is BlockHitResult) return false

            val target = mc.crosshairTarget as BlockHitResult
            val targetedState = cWorld.getBlockState(target.blockPos)
            val targetedBlock = targetedState.block

            return InventoryUtil.findHand<ToolItem>() == Hand.MAIN_HAND
                    && mc.crosshairTarget != null
                    && !targetedState.isAir
                    && targetedBlock.hardness > -1 && targetedBlock.hardness <= 50
                    && cInteractManager.isBreakingBlock
        }
    val isConsuming: Boolean
        get() {
            val item = cPlayer.activeItem
            val eatsOrDrinks = item.useAction == UseAction.EAT || item.useAction == UseAction.DRINK
            return eatsOrDrinks && cPlayer.isUsingItem
        }

    val isMending: Boolean
        get() {
            cPlayer.run {
                val ench = Enchantments.MENDING
                val armor = armorItems.any { it.isDamaged } && hasEnchantmentOnArmor(ench)
                val hands = cPlayer.handItems.any { it.isDamaged && it.hasEnchantment(ench) }
                val hasBottle = cPlayer.handItems.any { it.isOf(Items.EXPERIENCE_BOTTLE) }
                return (armor || hands) && hasBottle
            }
        }

    val isFlying: Boolean
        get() = cPlayer.isFallFlying || cPlayer.abilities.flying || Flight.enabled

    val isMovingH get() = cPlayer.input.run { movementForward != 0f || movementSideways != 0f }
    val isMoving get() = isMovingH || mc.options.run { jumpKey.isPressed || sneakKey.isPressed }

    fun isInLiquid() = cPlayer.isSubmergedInWater || cPlayer.isInLava || cPlayer.isInsideWaterOrBubbleColumn || cPlayer.isSubmergedIn(FluidTags.LAVA)

    fun SettingGroup.process(event: EventPacket, method: () -> Unit) {
        if (!this.toggled) return

        val resultList = mutableListOf<SettingMap>()
        this.settings.flatMap { (it as SettingGroup).settings }.forEach {
            if (it is SettingGroup) resultList += it.settings as List<SettingMap>
            else resultList += it as SettingMap
        }

        resultList.forEach {
            if (event.packet::class.java == it.key && it.toggled) method()
        }
    }
}