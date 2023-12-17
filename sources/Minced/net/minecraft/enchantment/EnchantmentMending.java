// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentMending extends Enchantment
{
    public EnchantmentMending(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
        this.setName("mending");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return enchantmentLevel * 25;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
