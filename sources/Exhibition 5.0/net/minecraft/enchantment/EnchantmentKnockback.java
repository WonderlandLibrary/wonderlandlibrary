// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentKnockback extends Enchantment
{
    private static final String __OBFID = "CL_00000118";
    
    protected EnchantmentKnockback(final int p_i45768_1_, final ResourceLocation p_i45768_2_, final int p_i45768_3_) {
        super(p_i45768_1_, p_i45768_2_, p_i45768_3_, EnumEnchantmentType.WEAPON);
        this.setName("knockback");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 5 + 20 * (p_77321_1_ - 1);
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
