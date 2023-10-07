// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;

public enum EnumEnchantmentType
{
    ALL("ALL", 0), 
    ARMOR("ARMOR", 1), 
    ARMOR_FEET("ARMOR_FEET", 2), 
    ARMOR_LEGS("ARMOR_LEGS", 3), 
    ARMOR_TORSO("ARMOR_TORSO", 4), 
    ARMOR_HEAD("ARMOR_HEAD", 5), 
    WEAPON("WEAPON", 6), 
    DIGGER("DIGGER", 7), 
    FISHING_ROD("FISHING_ROD", 8), 
    BREAKABLE("BREAKABLE", 9), 
    BOW("BOW", 10);
    
    private static final EnumEnchantmentType[] $VALUES;
    private static final String __OBFID = "CL_00000106";
    
    private EnumEnchantmentType(final String p_i1927_1_, final int p_i1927_2_) {
    }
    
    public boolean canEnchantItem(final Item p_77557_1_) {
        if (this == EnumEnchantmentType.ALL) {
            return true;
        }
        if (this == EnumEnchantmentType.BREAKABLE && p_77557_1_.isDamageable()) {
            return true;
        }
        if (!(p_77557_1_ instanceof ItemArmor)) {
            return (p_77557_1_ instanceof ItemSword) ? (this == EnumEnchantmentType.WEAPON) : ((p_77557_1_ instanceof ItemTool) ? (this == EnumEnchantmentType.DIGGER) : ((p_77557_1_ instanceof ItemBow) ? (this == EnumEnchantmentType.BOW) : (p_77557_1_ instanceof ItemFishingRod && this == EnumEnchantmentType.FISHING_ROD)));
        }
        if (this == EnumEnchantmentType.ARMOR) {
            return true;
        }
        final ItemArmor var2 = (ItemArmor)p_77557_1_;
        return (var2.armorType == 0) ? (this == EnumEnchantmentType.ARMOR_HEAD) : ((var2.armorType == 2) ? (this == EnumEnchantmentType.ARMOR_LEGS) : ((var2.armorType == 1) ? (this == EnumEnchantmentType.ARMOR_TORSO) : (var2.armorType == 3 && this == EnumEnchantmentType.ARMOR_FEET)));
    }
    
    static {
        $VALUES = new EnumEnchantmentType[] { EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.WEAPON, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE, EnumEnchantmentType.BOW };
    }
}
