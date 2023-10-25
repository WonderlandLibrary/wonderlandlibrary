/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowFire
extends Enchantment {
    @Override
    public int getMinEnchantability(int n) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return 50;
    }

    public EnchantmentArrowFire(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.BOW);
        this.setName("arrowFire");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}

