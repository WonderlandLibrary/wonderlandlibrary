// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemArmor;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability extends Enchantment
{
    private static final String __OBFID = "CL_00000103";
    
    protected EnchantmentDurability(final int p_i45773_1_, final ResourceLocation p_i45773_2_, final int p_i45773_3_) {
        super(p_i45773_1_, p_i45773_2_, p_i45773_3_, EnumEnchantmentType.BREAKABLE);
        this.setName("durability");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 5 + (p_77321_1_ - 1) * 8;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canApply(final ItemStack p_92089_1_) {
        return p_92089_1_.isItemStackDamageable() || super.canApply(p_92089_1_);
    }
    
    public static boolean negateDamage(final ItemStack p_92097_0_, final int p_92097_1_, final Random p_92097_2_) {
        return (!(p_92097_0_.getItem() instanceof ItemArmor) || p_92097_2_.nextFloat() >= 0.6f) && p_92097_2_.nextInt(p_92097_1_ + 1) > 0;
    }
}
