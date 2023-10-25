/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentUntouching
extends Enchantment {
    @Override
    public int getMaxLevel() {
        return 1;
    }

    protected EnchantmentUntouching(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.DIGGER);
        this.setName("untouching");
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.getItem() == Items.shears ? true : super.canApply(itemStack);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment.effectId != EnchantmentUntouching.fortune.effectId;
    }

    @Override
    public int getMinEnchantability(int n) {
        return 15;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }
}

