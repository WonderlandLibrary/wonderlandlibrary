// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentWaterWorker extends Enchantment
{
    public EnchantmentWaterWorker(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR_HEAD, slots);
        this.setName("waterWorker");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 1;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
