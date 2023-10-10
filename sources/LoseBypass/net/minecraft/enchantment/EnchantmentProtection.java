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
    private static final String[] protectionName = new String[]{"all", "fire", "fall", "explosion", "projectile"};
    private static final int[] baseEnchantability = new int[]{1, 10, 5, 5, 3};
    private static final int[] levelEnchantability = new int[]{11, 8, 6, 8, 6};
    private static final int[] thresholdEnchantability = new int[]{20, 12, 10, 12, 15};
    public final int protectionType;

    public EnchantmentProtection(int p_i45765_1_, ResourceLocation p_i45765_2_, int p_i45765_3_, int p_i45765_4_) {
        super(p_i45765_1_, p_i45765_2_, p_i45765_3_, EnumEnchantmentType.ARMOR);
        this.protectionType = p_i45765_4_;
        if (p_i45765_4_ != 2) return;
        this.type = EnumEnchantmentType.ARMOR_FEET;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return baseEnchantability[this.protectionType] + (enchantmentLevel - 1) * levelEnchantability[this.protectionType];
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.protectionType];
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        int n;
        if (source.canHarmInCreative()) {
            return 0;
        }
        float f = (float)(6 + level * level) / 3.0f;
        if (this.protectionType == 0) {
            n = MathHelper.floor_float(f * 0.75f);
            return n;
        }
        if (this.protectionType == 1 && source.isFireDamage()) {
            n = MathHelper.floor_float(f * 1.25f);
            return n;
        }
        if (this.protectionType == 2 && source == DamageSource.fall) {
            n = MathHelper.floor_float(f * 2.5f);
            return n;
        }
        if (this.protectionType == 3 && source.isExplosion()) {
            n = MathHelper.floor_float(f * 1.5f);
            return n;
        }
        if (this.protectionType != 4) return 0;
        if (!source.isProjectile()) return 0;
        n = MathHelper.floor_float(f * 1.5f);
        return n;
    }

    @Override
    public String getName() {
        return "enchantment.protect." + protectionName[this.protectionType];
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        if (!(ench instanceof EnchantmentProtection)) return super.canApplyTogether(ench);
        EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
        if (enchantmentprotection.protectionType == this.protectionType) {
            return false;
        }
        if (this.protectionType == 2) return true;
        if (enchantmentprotection.protectionType == 2) return true;
        return false;
    }

    public static int getFireTimeForEntity(Entity p_92093_0_, int p_92093_1_) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, p_92093_0_.getInventory());
        if (i <= 0) return p_92093_1_;
        p_92093_1_ -= MathHelper.floor_float((float)p_92093_1_ * (float)i * 0.15f);
        return p_92093_1_;
    }

    public static double func_92092_a(Entity p_92092_0_, double p_92092_1_) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, p_92092_0_.getInventory());
        if (i <= 0) return p_92092_1_;
        p_92092_1_ -= (double)MathHelper.floor_double(p_92092_1_ * (double)((float)i * 0.15f));
        return p_92092_1_;
    }
}

