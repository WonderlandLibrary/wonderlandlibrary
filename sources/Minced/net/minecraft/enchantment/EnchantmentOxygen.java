// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentOxygen extends Enchantment
{
    public EnchantmentOxygen(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR_HEAD, slots);
        this.setName("oxygen");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 10 * enchantmentLevel;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
