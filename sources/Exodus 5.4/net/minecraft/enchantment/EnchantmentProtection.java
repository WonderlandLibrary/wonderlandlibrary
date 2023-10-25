/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class EnchantmentProtection
extends Enchantment {
    private static final int[] thresholdEnchantability;
    private static final int[] baseEnchantability;
    public final int protectionType;
    private static final String[] protectionName;
    private static final int[] levelEnchantability;

    @Override
    public int getMinEnchantability(int n) {
        return baseEnchantability[this.protectionType] + (n - 1) * levelEnchantability[this.protectionType];
    }

    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + thresholdEnchantability[this.protectionType];
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int calcModifierDamage(int n, DamageSource damageSource) {
        if (damageSource.canHarmInCreative()) {
            return 0;
        }
        float f = (float)(6 + n * n) / 3.0f;
        return this.protectionType == 0 ? MathHelper.floor_float(f * 0.75f) : (this.protectionType == 1 && damageSource.isFireDamage() ? MathHelper.floor_float(f * 1.25f) : (this.protectionType == 2 && damageSource == DamageSource.fall ? MathHelper.floor_float(f * 2.5f) : (this.protectionType == 3 && damageSource.isExplosion() ? MathHelper.floor_float(f * 1.5f) : (this.protectionType == 4 && damageSource.isProjectile() ? MathHelper.floor_float(f * 1.5f) : 0))));
    }

    static {
        protectionName = new String[]{"all", "fire", "fall", "explosion", "projectile"};
        baseEnchantability = new int[]{1, 10, 5, 5, 3};
        levelEnchantability = new int[]{11, 8, 6, 8, 6};
        thresholdEnchantability = new int[]{20, 12, 10, 12, 15};
    }

    public static double func_92092_a(Entity entity, double d) {
        int n = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, entity.getInventory());
        if (n > 0) {
            d -= (double)MathHelper.floor_double(d * (double)((float)n * 0.15f));
        }
        return d;
    }

    public static int getFireTimeForEntity(Entity entity, int n) {
        int n2 = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, entity.getInventory());
        if (n2 > 0) {
            n -= MathHelper.floor_float((float)n * (float)n2 * 0.15f);
        }
        return n;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentProtection) {
            EnchantmentProtection enchantmentProtection = (EnchantmentProtection)enchantment;
            return enchantmentProtection.protectionType == this.protectionType ? false : this.protectionType == 2 || enchantmentProtection.protectionType == 2;
        }
        return super.canApplyTogether(enchantment);
    }

    @Override
    public String getName() {
        return "enchantment.protect." + protectionName[this.protectionType];
    }

    public EnchantmentProtection(int n, ResourceLocation resourceLocation, int n2, int n3) {
        super(n, resourceLocation, n2, EnumEnchantmentType.ARMOR);
        this.protectionType = n3;
        if (n3 == 2) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }
}

