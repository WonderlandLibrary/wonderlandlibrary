// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowKnockback extends Enchantment
{
    public EnchantmentArrowKnockback(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowKnockback");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 12 + (enchantmentLevel - 1) * 20;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
