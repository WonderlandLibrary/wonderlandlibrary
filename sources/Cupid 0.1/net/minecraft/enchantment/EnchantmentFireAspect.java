package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFireAspect extends Enchantment {
  protected EnchantmentFireAspect(int enchID, ResourceLocation enchName, int enchWeight) {
    super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
    setName("fire");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 10 + 20 * (enchantmentLevel - 1);
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentFireAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */