/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentKnockback
extends Enchantment {
    private static final String __OBFID = "CL_00000118";

    protected EnchantmentKnockback(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_) {
        super(p_i45768_1_, p_i45768_2_, p_i45768_3_, EnumEnchantmentType.WEAPON);
        this.setName("knockback");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 5 + 20 * (p_77321_1_ - 1);
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}

