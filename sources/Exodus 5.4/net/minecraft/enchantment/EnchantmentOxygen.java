/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentOxygen
extends Enchantment {
    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public EnchantmentOxygen(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("oxygen");
    }

    @Override
    public int getMinEnchantability(int n) {
        return 10 * n;
    }
}

