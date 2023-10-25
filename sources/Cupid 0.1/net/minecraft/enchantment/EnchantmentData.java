package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item {
  public final Enchantment enchantmentobj;
  
  public final int enchantmentLevel;
  
  public EnchantmentData(Enchantment enchantmentObj, int enchLevel) {
    super(enchantmentObj.getWeight());
    this.enchantmentobj = enchantmentObj;
    this.enchantmentLevel = enchLevel;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */