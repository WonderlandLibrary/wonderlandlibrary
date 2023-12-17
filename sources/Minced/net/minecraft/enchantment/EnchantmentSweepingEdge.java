// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSweepingEdge extends Enchantment
{
    public EnchantmentSweepingEdge(final Rarity p_i47366_1_, final EntityEquipmentSlot... p_i47366_2_) {
        super(p_i47366_1_, EnumEnchantmentType.WEAPON, p_i47366_2_);
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 9;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    public static float getSweepingDamageRatio(final int p_191526_0_) {
        return 1.0f - 1.0f / (p_191526_0_ + 1);
    }
    
    @Override
    public String getName() {
        return "enchantment.sweeping";
    }
}
