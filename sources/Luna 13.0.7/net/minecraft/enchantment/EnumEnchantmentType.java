package net.minecraft.enchantment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public enum EnumEnchantmentType
{
  private static final EnumEnchantmentType[] $VALUES = { ALL, ARMOR, ARMOR_FEET, ARMOR_LEGS, ARMOR_TORSO, ARMOR_HEAD, WEAPON, DIGGER, FISHING_ROD, BREAKABLE, BOW };
  private static final String __OBFID = "CL_00000106";
  
  public boolean canEnchantItem(Item p_77557_1_)
  {
    if (this == ALL) {
      return true;
    }
    if ((this == BREAKABLE) && (p_77557_1_.isDamageable())) {
      return true;
    }
    if ((p_77557_1_ instanceof ItemArmor))
    {
      if (this == ARMOR) {
        return true;
      }
      ItemArmor var2 = (ItemArmor)p_77557_1_;
      return this == ARMOR_HEAD;
    }
    return this == WEAPON;
  }
}
