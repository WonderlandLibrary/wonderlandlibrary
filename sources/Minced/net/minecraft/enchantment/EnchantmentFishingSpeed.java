// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFishingSpeed extends Enchantment
{
    protected EnchantmentFishingSpeed(final Rarity rarityIn, final EnumEnchantmentType typeIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, typeIn, slots);
        this.setName("fishingSpeed");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
