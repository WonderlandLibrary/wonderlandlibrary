/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentBindingCurse
extends Enchantment {
    public EnchantmentBindingCurse(Enchantment.Rarity p_i47254_1_, EntityEquipmentSlot ... p_i47254_2_) {
        super(p_i47254_1_, EnumEnchantmentType.WEARABLE, p_i47254_2_);
        this.setName("binding_curse");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
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
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean func_190936_d() {
        return true;
    }
}

