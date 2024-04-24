package me.offeex.bloomware.api.util

import me.offeex.bloomware.mixin.accessor.IAnimalEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.item.ItemStack
import java.util.*

object EntityUtil {
    fun LivingEntity.getOwnerUUID(): UUID? {
        return when (this) {
            is TameableEntity -> ownerUuid
            is AbstractHorseEntity -> ownerUuid
            is AnimalEntity -> (this as IAnimalEntity).lovingPlayer
            else -> null
        }
    }

    fun LivingEntity.getFullHealth() = health + absorptionAmount
    fun ItemStack.hasEnchantment(ench: Enchantment) = EnchantmentHelper.getLevel(ench, this) > 0
    fun LivingEntity.hasEnchantmentOnArmor(ench: Enchantment) = EnchantmentHelper.getEquipmentLevel(ench, this) > 0
}