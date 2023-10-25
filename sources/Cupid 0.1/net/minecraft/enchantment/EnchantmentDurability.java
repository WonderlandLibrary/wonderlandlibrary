package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability extends Enchantment {
  protected EnchantmentDurability(int enchID, ResourceLocation enchName, int enchWeight) {
    super(enchID, enchName, enchWeight, EnumEnchantmentType.BREAKABLE);
    setName("durability");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 5 + (enchantmentLevel - 1) * 8;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public boolean canApply(ItemStack stack) {
    return stack.isItemStackDamageable() ? true : super.canApply(stack);
  }
  
  public static boolean negateDamage(ItemStack p_92097_0_, int p_92097_1_, Random p_92097_2_) {
    return (p_92097_0_.getItem() instanceof net.minecraft.item.ItemArmor && p_92097_2_.nextFloat() < 0.6F) ? false : ((p_92097_2_.nextInt(p_92097_1_ + 1) > 0));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentDurability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */