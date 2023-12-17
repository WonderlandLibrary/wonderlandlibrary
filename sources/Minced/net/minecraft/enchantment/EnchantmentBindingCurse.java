// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentBindingCurse extends Enchantment
{
    public EnchantmentBindingCurse(final Rarity p_i47254_1_, final EntityEquipmentSlot... p_i47254_2_) {
        super(p_i47254_1_, EnumEnchantmentType.WEARABLE, p_i47254_2_);
        this.setName("binding_curse");
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
