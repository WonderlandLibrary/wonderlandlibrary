// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowInfinite extends Enchantment
{
    private static final String __OBFID = "CL_00000100";
    
    public EnchantmentArrowInfinite(final int p_i45776_1_, final ResourceLocation p_i45776_2_, final int p_i45776_3_) {
        super(p_i45776_1_, p_i45776_2_, p_i45776_3_, EnumEnchantmentType.BOW);
        this.setName("arrowInfinite");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 20;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
