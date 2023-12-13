// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentUntouching extends Enchantment
{
    private static final String __OBFID = "CL_00000123";
    
    protected EnchantmentUntouching(final int p_i45763_1_, final ResourceLocation p_i45763_2_, final int p_i45763_3_) {
        super(p_i45763_1_, p_i45763_2_, p_i45763_3_, EnumEnchantmentType.DIGGER);
        this.setName("untouching");
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 15;
    }
    
    @Override
    public int getMaxEnchantability(final int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_) && p_77326_1_.effectId != Enchantment.fortune.effectId;
    }
    
    @Override
    public boolean canApply(final ItemStack p_92089_1_) {
        return p_92089_1_.getItem() == Items.shears || super.canApply(p_92089_1_);
    }
}
