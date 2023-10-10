/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class WeightedRandomFishable
extends WeightedRandom.Item {
    private final ItemStack returnStack;
    private float maxDamagePercent;
    private boolean enchantable;

    public WeightedRandomFishable(ItemStack returnStackIn, int itemWeightIn) {
        super(itemWeightIn);
        this.returnStack = returnStackIn;
    }

    public ItemStack getItemStack(Random random) {
        ItemStack itemstack = this.returnStack.copy();
        if (this.maxDamagePercent > 0.0f) {
            int i2 = (int)(this.maxDamagePercent * (float)this.returnStack.getMaxDamage());
            int j2 = itemstack.getMaxDamage() - random.nextInt(random.nextInt(i2) + 1);
            if (j2 > i2) {
                j2 = i2;
            }
            if (j2 < 1) {
                j2 = 1;
            }
            itemstack.setItemDamage(j2);
        }
        if (this.enchantable) {
            EnchantmentHelper.addRandomEnchantment(random, itemstack, 30);
        }
        return itemstack;
    }

    public WeightedRandomFishable setMaxDamagePercent(float maxDamagePercentIn) {
        this.maxDamagePercent = maxDamagePercentIn;
        return this;
    }

    public WeightedRandomFishable setEnchantable() {
        this.enchantable = true;
        return this;
    }
}

