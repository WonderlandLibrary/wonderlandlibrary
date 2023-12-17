// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemElytra;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;

public enum EnumEnchantmentType
{
    ALL {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            for (final EnumEnchantmentType enumenchantmenttype : EnumEnchantmentType.values()) {
                if (enumenchantmenttype != EnumEnchantmentType.ALL && enumenchantmenttype.canEnchantItem(itemIn)) {
                    return true;
                }
            }
            return false;
        }
    }, 
    ARMOR {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemArmor;
        }
    }, 
    ARMOR_FEET {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.FEET;
        }
    }, 
    ARMOR_LEGS {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.LEGS;
        }
    }, 
    ARMOR_CHEST {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.CHEST;
        }
    }, 
    ARMOR_HEAD {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.HEAD;
        }
    }, 
    WEAPON {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemSword;
        }
    }, 
    DIGGER {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemTool;
        }
    }, 
    FISHING_ROD {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemFishingRod;
        }
    }, 
    BREAKABLE {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn.isDamageable();
        }
    }, 
    BOW {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            return itemIn instanceof ItemBow;
        }
    }, 
    WEARABLE {
        @Override
        public boolean canEnchantItem(final Item itemIn) {
            final boolean flag = itemIn instanceof ItemBlock && ((ItemBlock)itemIn).getBlock() instanceof BlockPumpkin;
            return itemIn instanceof ItemArmor || itemIn instanceof ItemElytra || itemIn instanceof ItemSkull || flag;
        }
    };
    
    public abstract boolean canEnchantItem(final Item p0);
}
