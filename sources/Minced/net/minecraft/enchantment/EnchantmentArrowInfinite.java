// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowInfinite extends Enchantment
{
    public EnchantmentArrowInfinite(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowInfinite");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 20;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return !(ench instanceof EnchantmentMending) && super.canApplyTogether(ench);
    }
}
