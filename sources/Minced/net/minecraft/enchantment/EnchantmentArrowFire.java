// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowFire extends Enchantment
{
    public EnchantmentArrowFire(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowFire");
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
}
