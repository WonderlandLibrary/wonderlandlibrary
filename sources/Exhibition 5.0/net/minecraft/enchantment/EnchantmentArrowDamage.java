// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowDamage extends Enchantment
{
    private static final String __OBFID = "CL_00000098";
    
    public EnchantmentArrowDamage(final int p_i45778_1_, final ResourceLocation p_i45778_2_, final int p_i45778_3_) {
        super(p_i45778_1_, p_i45778_2_, p_i45778_3_, EnumEnchantmentType.BOW);
        this.setName("arrowDamage");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 1 + (p_77321_1_ - 1) * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
