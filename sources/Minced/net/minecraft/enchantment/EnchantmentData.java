// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item
{
    public final Enchantment enchantment;
    public final int enchantmentLevel;
    
    public EnchantmentData(final Enchantment enchantmentObj, final int enchLevel) {
        super(enchantmentObj.getRarity().getWeight());
        this.enchantment = enchantmentObj;
        this.enchantmentLevel = enchLevel;
    }
}
