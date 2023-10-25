/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus
extends Enchantment {
    protected EnchantmentLootBonus(int n, ResourceLocation resourceLocation, int n2, EnumEnchantmentType enumEnchantmentType) {
        super(n, resourceLocation, n2, enumEnchantmentType);
        if (enumEnchantmentType == EnumEnchantmentType.DIGGER) {
            this.setName("lootBonusDigger");
        } else if (enumEnchantmentType == EnumEnchantmentType.FISHING_ROD) {
            this.setName("lootBonusFishing");
        } else {
            this.setName("lootBonus");
        }
    }

    @Override
    public int getMinEnchantability(int n) {
        return 15 + (n - 1) * 9;
    }

    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment.effectId != EnchantmentLootBonus.silkTouch.effectId;
    }
}

