package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWorker extends Enchantment {
  public EnchantmentWaterWorker(int p_i45761_1_, ResourceLocation p_i45761_2_, int p_i45761_3_) {
    super(p_i45761_1_, p_i45761_2_, p_i45761_3_, EnumEnchantmentType.ARMOR_HEAD);
    setName("waterWorker");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 1;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 40;
  }
  
  public int getMaxLevel() {
    return 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentWaterWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */