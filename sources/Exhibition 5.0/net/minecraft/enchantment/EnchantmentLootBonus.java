// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus extends Enchantment
{
    private static final String __OBFID = "CL_00000119";
    
    protected EnchantmentLootBonus(final int p_i45767_1_, final ResourceLocation p_i45767_2_, final int p_i45767_3_, final EnumEnchantmentType p_i45767_4_) {
        super(p_i45767_1_, p_i45767_2_, p_i45767_3_, p_i45767_4_);
        if (p_i45767_4_ == EnumEnchantmentType.DIGGER) {
            this.setName("lootBonusDigger");
        }
        else if (p_i45767_4_ == EnumEnchantmentType.FISHING_ROD) {
            this.setName("lootBonusFishing");
        }
        else {
            this.setName("lootBonus");
        }
    }
    
    @Override
    public int getMinEnchantability(final int p_77321_1_) {
        return 15 + (p_77321_1_ - 1) * 9;
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
    public boolean canApplyTogether(final Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_) && p_77326_1_.effectId != Enchantment.silkTouch.effectId;
    }
}
