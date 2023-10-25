package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFishingSpeed extends Enchantment {
  protected EnchantmentFishingSpeed(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType) {
    super(enchID, enchName, enchWeight, enchType);
    setName("fishingSpeed");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 15 + (enchantmentLevel - 1) * 9;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentFishingSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */