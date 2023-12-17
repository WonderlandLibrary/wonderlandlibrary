// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentLootBonus extends Enchantment
{
    protected EnchantmentLootBonus(final Rarity rarityIn, final EnumEnchantmentType typeIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, typeIn, slots);
        if (typeIn == EnumEnchantmentType.DIGGER) {
            this.setName("lootBonusDigger");
        }
        else if (typeIn == EnumEnchantmentType.FISHING_ROD) {
            this.setName("lootBonusFishing");
        }
        else {
            this.setName("lootBonus");
        }
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
    
    public boolean canApplyTogether(final Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH;
    }
}
