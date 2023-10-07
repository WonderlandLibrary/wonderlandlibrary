// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowFire extends Enchantment
{
    private static final String __OBFID = "CL_00000099";
    
    public EnchantmentArrowFire(final int p_i45777_1_, final ResourceLocation p_i45777_2_, final int p_i45777_3_) {
        super(p_i45777_1_, p_i45777_2_, p_i45777_3_, EnumEnchantmentType.BOW);
        this.setName("arrowFire");
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
