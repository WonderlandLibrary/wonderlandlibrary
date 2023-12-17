// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentVanishingCurse extends Enchantment
{
    public EnchantmentVanishingCurse(final Rarity p_i47252_1_, final EntityEquipmentSlot... p_i47252_2_) {
        super(p_i47252_1_, EnumEnchantmentType.ALL, p_i47252_2_);
        this.setName("vanishing_curse");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 25;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
    
    @Override
    public boolean isCurse() {
        return true;
    }
}
