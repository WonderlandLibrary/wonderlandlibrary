// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWalker extends Enchantment
{
    private static final String __OBFID = "CL_00002155";
    
    public EnchantmentWaterWalker(final int p_i45762_1_, final ResourceLocation p_i45762_2_, final int p_i45762_3_) {
        super(p_i45762_1_, p_i45762_2_, p_i45762_3_, EnumEnchantmentType.ARMOR_FEET);
        this.setName("waterWalker");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return p_77321_1_ * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
