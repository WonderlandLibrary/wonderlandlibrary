// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDamage extends Enchantment
{
    private static final String[] protectionName;
    private static final int[] baseEnchantability;
    private static final int[] levelEnchantability;
    private static final int[] thresholdEnchantability;
    public final int damageType;
    private static final String __OBFID = "CL_00000102";
    
    public EnchantmentDamage(final int p_i45774_1_, final ResourceLocation p_i45774_2_, final int p_i45774_3_, final int p_i45774_4_) {
        super(p_i45774_1_, p_i45774_2_, p_i45774_3_, EnumEnchantmentType.WEAPON);
        this.damageType = p_i45774_4_;
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return EnchantmentDamage.baseEnchantability[this.damageType] + (p_77321_1_ - 1) * EnchantmentDamage.levelEnchantability[this.damageType];
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + EnchantmentDamage.thresholdEnchantability[this.damageType];
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float func_152376_a(final int p_152376_1_, final EnumCreatureAttribute p_152376_2_) {
        return (this.damageType == 0) ? (p_152376_1_ * 1.25f) : ((this.damageType == 1 && p_152376_2_ == EnumCreatureAttribute.UNDEAD) ? (p_152376_1_ * 2.5f) : ((this.damageType == 2 && p_152376_2_ == EnumCreatureAttribute.ARTHROPOD) ? (p_152376_1_ * 2.5f) : 0.0f));
    }
    
    @Override
    public String getName() {
        return "enchantment.damage." + EnchantmentDamage.protectionName[this.damageType];
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment p_77326_1_) {
        return !(p_77326_1_ instanceof EnchantmentDamage);
    }
    
    @Override
    public boolean canApply(final ItemStack p_92089_1_) {
        return p_92089_1_.getItem() instanceof ItemAxe || super.canApply(p_92089_1_);
    }
    
    @Override
    public void func_151368_a(final EntityLivingBase p_151368_1_, final Entity p_151368_2_, final int p_151368_3_) {
        if (p_151368_2_ instanceof EntityLivingBase) {
            final EntityLivingBase var4 = (EntityLivingBase)p_151368_2_;
            if (this.damageType == 2 && var4.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                final int var5 = 20 + p_151368_1_.getRNG().nextInt(10 * p_151368_3_);
                var4.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, var5, 3));
            }
        }
    }
    
    static {
        protectionName = new String[] { "all", "undead", "arthropods" };
        baseEnchantability = new int[] { 1, 5, 5 };
        levelEnchantability = new int[] { 11, 8, 8 };
        thresholdEnchantability = new int[] { 20, 20, 20 };
    }
}
