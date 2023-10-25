/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowInfinite
extends Enchantment {
    public EnchantmentArrowInfinite(Enchantment.Rarity rarityIn, EntityEquipmentSlot ... slots) {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowInfinite");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench instanceof EnchantmentMending ? false : super.canApplyTogether(ench);
    }
}

