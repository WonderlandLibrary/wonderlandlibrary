/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWalker
extends Enchantment {
    @Override
    public int getMinEnchantability(int n) {
        return n * 10;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public EnchantmentWaterWalker(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.ARMOR_FEET);
        this.setName("waterWalker");
    }
}

