/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFishingSpeed
extends Enchantment {
    protected EnchantmentFishingSpeed(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot ... slots) {
        super(rarityIn, typeIn, slots);
        this.setName("fishingSpeed");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

