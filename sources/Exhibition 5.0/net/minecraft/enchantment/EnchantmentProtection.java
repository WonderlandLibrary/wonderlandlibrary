// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EnchantmentProtection extends Enchantment
{
    private static final String[] protectionName;
    private static final int[] baseEnchantability;
    private static final int[] levelEnchantability;
    private static final int[] thresholdEnchantability;
    public final int protectionType;
    private static final String __OBFID = "CL_00000121";
    
    public EnchantmentProtection(final int p_i45765_1_, final ResourceLocation p_i45765_2_, final int p_i45765_3_, final int p_i45765_4_) {
        super(p_i45765_1_, p_i45765_2_, p_i45765_3_, EnumEnchantmentType.ARMOR);
        this.protectionType = p_i45765_4_;
        if (p_i45765_4_ == 2) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return EnchantmentProtection.baseEnchantability[this.protectionType] + (p_77321_1_ - 1) * EnchantmentProtection.levelEnchantability[this.protectionType];
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + EnchantmentProtection.thresholdEnchantability[this.protectionType];
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public int calcModifierDamage(final int p_77318_1_, final DamageSource p_77318_2_) {
        if (p_77318_2_.canHarmInCreative()) {
            return 0;
        }
        final float var3 = (6 + p_77318_1_ * p_77318_1_) / 3.0f;
        return (this.protectionType == 0) ? MathHelper.floor_float(var3 * 0.75f) : ((this.protectionType == 1 && p_77318_2_.isFireDamage()) ? MathHelper.floor_float(var3 * 1.25f) : ((this.protectionType == 2 && p_77318_2_ == DamageSource.fall) ? MathHelper.floor_float(var3 * 2.5f) : ((this.protectionType == 3 && p_77318_2_.isExplosion()) ? MathHelper.floor_float(var3 * 1.5f) : ((this.protectionType == 4 && p_77318_2_.isProjectile()) ? MathHelper.floor_float(var3 * 1.5f) : 0))));
    }
    
    @Override
    public String getName() {
        return "enchantment.protect." + EnchantmentProtection.protectionName[this.protectionType];
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment p_77326_1_) {
        if (p_77326_1_ instanceof EnchantmentProtection) {
            final EnchantmentProtection var2 = (EnchantmentProtection)p_77326_1_;
            return var2.protectionType != this.protectionType && (this.protectionType == 2 || var2.protectionType == 2);
        }
        return super.canApplyTogether(p_77326_1_);
    }
    
    public static int getFireTimeForEntity(final Entity p_92093_0_, int p_92093_1_) {
        final int var2 = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, p_92093_0_.getInventory());
        if (var2 > 0) {
            p_92093_1_ -= MathHelper.floor_float(p_92093_1_ * var2 * 0.15f);
        }
        return p_92093_1_;
    }
    
    public static double func_92092_a(final Entity p_92092_0_, double p_92092_1_) {
        final int var3 = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, p_92092_0_.getInventory());
        if (var3 > 0) {
            p_92092_1_ -= MathHelper.floor_double(p_92092_1_ * (var3 * 0.15f));
        }
        return p_92092_1_;
    }
    
    static {
        protectionName = new String[] { "all", "fire", "fall", "explosion", "projectile" };
        baseEnchantability = new int[] { 1, 10, 5, 5, 3 };
        levelEnchantability = new int[] { 11, 8, 6, 8, 6 };
        thresholdEnchantability = new int[] { 20, 12, 10, 12, 15 };
    }
}
