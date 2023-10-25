/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSweepingEdge
extends Enchantment {
    public EnchantmentSweepingEdge(Enchantment.Rarity p_i47366_1_, EntityEquipmentSlot ... p_i47366_2_) {
        super(p_i47366_1_, EnumEnchantmentType.WEAPON, p_i47366_2_);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 9;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static float func_191526_e(int p_191526_0_) {
        return 1.0f - 1.0f / (float)(p_191526_0_ + 1);
    }

    @Override
    public String getName() {
        return "enchantment.sweeping";
    }
}

