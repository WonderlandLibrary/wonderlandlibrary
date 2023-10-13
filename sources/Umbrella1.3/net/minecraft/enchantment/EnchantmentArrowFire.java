/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowFire
extends Enchantment {
    private static final String __OBFID = "CL_00000099";

    public EnchantmentArrowFire(int p_i45777_1_, ResourceLocation p_i45777_2_, int p_i45777_3_) {
        super(p_i45777_1_, p_i45777_2_, p_i45777_3_, EnumEnchantmentType.BOW);
        this.setName("arrowFire");
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

