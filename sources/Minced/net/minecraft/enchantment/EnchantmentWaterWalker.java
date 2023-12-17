// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentWaterWalker extends Enchantment
{
    public EnchantmentWaterWalker(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR_FEET, slots);
        this.setName("waterWalker");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return enchantmentLevel * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.FROST_WALKER;
    }
}
