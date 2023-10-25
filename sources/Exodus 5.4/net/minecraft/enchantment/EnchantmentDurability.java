/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability
extends Enchantment {
    protected EnchantmentDurability(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.BREAKABLE);
        this.setName("durability");
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.isItemStackDamageable() ? true : super.canApply(itemStack);
    }

    public static boolean negateDamage(ItemStack itemStack, int n, Random random) {
        return itemStack.getItem() instanceof ItemArmor && random.nextFloat() < 0.6f ? false : random.nextInt(n + 1) > 0;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }

    @Override
    public int getMinEnchantability(int n) {
        return 5 + (n - 1) * 8;
    }
}

