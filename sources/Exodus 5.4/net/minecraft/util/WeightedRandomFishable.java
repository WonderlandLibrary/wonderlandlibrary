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
    private float maxDamagePercent;
    private final ItemStack returnStack;
    private boolean enchantable;

    public WeightedRandomFishable setEnchantable() {
        this.enchantable = true;
        return this;
    }

    public WeightedRandomFishable(ItemStack itemStack, int n) {
        super(n);
        this.returnStack = itemStack;
    }

    public WeightedRandomFishable setMaxDamagePercent(float f) {
        this.maxDamagePercent = f;
        return this;
    }

    public ItemStack getItemStack(Random random) {
        ItemStack itemStack = this.returnStack.copy();
        if (this.maxDamagePercent > 0.0f) {
            int n = (int)(this.maxDamagePercent * (float)this.returnStack.getMaxDamage());
            int n2 = itemStack.getMaxDamage() - random.nextInt(random.nextInt(n) + 1);
            if (n2 > n) {
                n2 = n;
            }
            if (n2 < 1) {
                n2 = 1;
            }
            itemStack.setItemDamage(n2);
        }
        if (this.enchantable) {
            EnchantmentHelper.addRandomEnchantment(random, itemStack, 30);
        }
        return itemStack;
    }
}

