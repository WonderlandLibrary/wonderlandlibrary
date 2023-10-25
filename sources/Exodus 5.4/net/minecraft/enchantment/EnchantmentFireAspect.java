/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentFireAspect
extends Enchantment {
    @Override
    public int getMinEnchantability(int n) {
        return 10 + 20 * (n - 1);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    protected EnchantmentFireAspect(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.WEAPON);
        this.setName("fire");
    }

    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }
}

