/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWalker
extends Enchantment {
    private static final String __OBFID = "CL_00002155";

    public EnchantmentWaterWalker(int p_i45762_1_, ResourceLocation p_i45762_2_, int p_i45762_3_) {
        super(p_i45762_1_, p_i45762_2_, p_i45762_3_, EnumEnchantmentType.ARMOR_FEET);
        this.setName("waterWalker");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return p_77321_1_ * 10;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

