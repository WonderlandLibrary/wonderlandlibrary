/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.WeightedRandom;

public class EnchantmentData
extends WeightedRandom.Item {
    public final int enchantmentLevel;
    public final Enchantment enchantmentobj;

    public EnchantmentData(Enchantment enchantment, int n) {
        super(enchantment.getWeight());
        this.enchantmentobj = enchantment;
        this.enchantmentLevel = n;
    }
}

