/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowDamage
extends Enchantment {
    public EnchantmentArrowDamage(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.BOW);
        this.setName("arrowDamage");
    }

    @Override
    public int getMinEnchantability(int n) {
        return 1 + (n - 1) * 10;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + 15;
    }
}

