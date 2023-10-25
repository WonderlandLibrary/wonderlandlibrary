package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowKnockback extends Enchantment {
  public EnchantmentArrowKnockback(int enchID, ResourceLocation enchName, int enchWeight) {
    super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
    setName("arrowKnockback");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 12 + (enchantmentLevel - 1) * 20;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 25;
  }
  
  public int getMaxLevel() {
    return 2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentArrowKnockback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */