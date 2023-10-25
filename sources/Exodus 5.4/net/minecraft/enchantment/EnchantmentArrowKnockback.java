/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowKnockback
extends Enchantment {
    @Override
    public int getMaxLevel() {
        return 2;
    }

    public EnchantmentArrowKnockback(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.BOW);
        this.setName("arrowKnockback");
    }

    @Override
    public int getMinEnchantability(int n) {
        return 12 + (n - 1) * 20;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + 25;
    }
}

