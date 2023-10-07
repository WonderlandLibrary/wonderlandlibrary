// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWorker extends Enchantment
{
    private static final String __OBFID = "CL_00000124";
    
    public EnchantmentWaterWorker(final int p_i45761_1_, final ResourceLocation p_i45761_2_, final int p_i45761_3_) {
        super(p_i45761_1_, p_i45761_2_, p_i45761_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("waterWorker");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 1;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 40;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
