/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum EnumEnchantmentType {
    ALL,
    ARMOR,
    ARMOR_FEET,
    ARMOR_LEGS,
    ARMOR_TORSO,
    ARMOR_HEAD,
    WEAPON,
    DIGGER,
    FISHING_ROD,
    BREAKABLE,
    BOW;


    public boolean canEnchantItem(Item p_77557_1_) {
        if (this == ALL) {
            return true;
        }
        if (this == BREAKABLE && p_77557_1_.isDamageable()) {
            return true;
        }
        if (p_77557_1_ instanceof ItemArmor) {
            if (this == ARMOR) {
                return true;
            }
            ItemArmor itemarmor = (ItemArmor)p_77557_1_;
            if (itemarmor.armorType == 0) {
                if (this != ARMOR_HEAD) return false;
                return true;
            }
            if (itemarmor.armorType == 2) {
                if (this != ARMOR_LEGS) return false;
                return true;
            }
            if (itemarmor.armorType == 1) {
                if (this != ARMOR_TORSO) return false;
                return true;
            }
            if (itemarmor.armorType != 3) {
                return false;
            }
            if (this != ARMOR_FEET) return false;
            return true;
        }
        if (p_77557_1_ instanceof ItemSword) {
            if (this != WEAPON) return false;
            return true;
        }
        if (p_77557_1_ instanceof ItemTool) {
            if (this != DIGGER) return false;
            return true;
        }
        if (p_77557_1_ instanceof ItemBow) {
            if (this != BOW) return false;
            return true;
        }
        if (!(p_77557_1_ instanceof ItemFishingRod)) {
            return false;
        }
        if (this != FISHING_ROD) return false;
        return true;
    }
}

