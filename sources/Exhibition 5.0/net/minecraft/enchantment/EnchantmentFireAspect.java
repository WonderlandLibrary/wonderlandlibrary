// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFireAspect extends Enchantment
{
    private static final String __OBFID = "CL_00000116";
    
    protected EnchantmentFireAspect(final int p_i45770_1_, final ResourceLocation p_i45770_2_, final int p_i45770_3_) {
        super(p_i45770_1_, p_i45770_2_, p_i45770_3_, EnumEnchantmentType.WEAPON);
        this.setName("fire");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 10 + 20 * (p_77321_1_ - 1);
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
