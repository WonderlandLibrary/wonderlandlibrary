package net.minecraft.enchantment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum EnumEnchantmentType {
   DIGGER;

   private static final EnumEnchantmentType[] ENUM$VALUES = new EnumEnchantmentType[]{ALL, ARMOR, ARMOR_FEET, ARMOR_LEGS, ARMOR_TORSO, ARMOR_HEAD, WEAPON, DIGGER, FISHING_ROD, BREAKABLE, BOW};
   ARMOR_LEGS,
   ALL,
   BREAKABLE,
   ARMOR_HEAD,
   BOW,
   ARMOR_FEET,
   FISHING_ROD,
   ARMOR,
   WEAPON,
   ARMOR_TORSO;

   public boolean canEnchantItem(Item var1) {
      if (this == ALL) {
         return true;
      } else if (this == BREAKABLE && var1.isDamageable()) {
         return true;
      } else if (var1 instanceof ItemArmor) {
         if (this == ARMOR) {
            return true;
         } else {
            ItemArmor var2 = (ItemArmor)var1;
            return var2.armorType == 0 ? this == ARMOR_HEAD : (var2.armorType == 2 ? this == ARMOR_LEGS : (var2.armorType == 1 ? this == ARMOR_TORSO : (var2.armorType == 3 ? this == ARMOR_FEET : false)));
         }
      } else {
         return var1 instanceof ItemSword ? this == WEAPON : (var1 instanceof ItemTool ? this == DIGGER : (var1 instanceof ItemBow ? this == BOW : (var1 instanceof ItemFishingRod ? this == FISHING_ROD : false)));
      }
   }
}
