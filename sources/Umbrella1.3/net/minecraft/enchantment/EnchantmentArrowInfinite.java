/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowInfinite
extends Enchantment {
    private static final String __OBFID = "CL_00000100";

    public EnchantmentArrowInfinite(int p_i45776_1_, ResourceLocation p_i45776_2_, int p_i45776_3_) {
        super(p_i45776_1_, p_i45776_2_, p_i45776_3_, EnumEnchantmentType.BOW);
        this.setName("arrowInfinite");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}

