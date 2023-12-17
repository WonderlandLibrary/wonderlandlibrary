// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentUntouching extends Enchantment
{
    protected EnchantmentUntouching(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.DIGGER, slots);
        this.setName("untouching");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 15;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.FORTUNE;
    }
}
