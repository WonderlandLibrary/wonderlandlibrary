/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDigging
extends Enchantment {
    private static final String __OBFID = "CL_00000104";

    protected EnchantmentDigging(int p_i45772_1_, ResourceLocation p_i45772_2_, int p_i45772_3_) {
        super(p_i45772_1_, p_i45772_2_, p_i45772_3_, EnumEnchantmentType.DIGGER);
        this.setName("digging");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 1 + 10 * (p_77321_1_ - 1);
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApply(ItemStack p_92089_1_) {
        return p_92089_1_.getItem() == Items.shears ? true : super.canApply(p_92089_1_);
    }
}

