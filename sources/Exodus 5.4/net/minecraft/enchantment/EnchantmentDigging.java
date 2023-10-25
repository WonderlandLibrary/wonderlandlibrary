/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDigging
extends Enchantment {
    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.getItem() == Items.shears ? true : super.canApply(itemStack);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }

    @Override
    public int getMinEnchantability(int n) {
        return 1 + 10 * (n - 1);
    }

    protected EnchantmentDigging(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.DIGGER);
        this.setName("digging");
    }
}

